package de.mle.sandbox.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;
import static org.springframework.kafka.test.assertj.KafkaConditions.partition;
import static org.springframework.kafka.test.assertj.KafkaConditions.value;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import de.mle.sandbox.EmbeddedKafkaInitializer;
import de.mle.sandbox.kafka.consumer.Receiver;
import de.mle.sandbox.kafka.producer.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class KafkaIT extends EmbeddedKafkaInitializer {
	private static final String TOPIC = "helloworld.t";

	@Autowired
	private Receiver receiver;

	@Autowired
	private Sender sender;

	@Autowired
	private KafkaEmbedded kafkaEmbedded;

	@Test
	public void testReceive() throws Exception {
		sender.send(TOPIC, "Hello Spring Kafka!");

		receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		assertThat(receiver.getLatch().getCount()).isEqualTo(0);
	}

	@Test
	public void testTemplate() throws Exception {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testT", "false", kafkaEmbedded);
		DefaultKafkaConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);

		ContainerProperties containerProperties = new ContainerProperties(TOPIC);
		KafkaMessageListenerContainer<Integer, String> container = new KafkaMessageListenerContainer<>(cf, containerProperties);

		BlockingQueue<ConsumerRecord<Integer, String>> records = new LinkedBlockingQueue<>();
		container.setupMessageListener(new MessageListener<Integer, String>() {

			@Override
			public void onMessage(ConsumerRecord<Integer, String> record) {
				System.out.println(record);
				records.add(record);
			}

		});
		container.setBeanName("templateTests");
		container.start();
		ContainerTestUtils.waitForAssignment(container, kafkaEmbedded.getPartitionsPerTopic());
		Map<String, Object> senderProps = KafkaTestUtils.senderProps(kafkaEmbedded.getBrokersAsString());

		ProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
		KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
		template.setDefaultTopic(TOPIC);

		template.sendDefault("foo");
		assertThat(records.poll(10, TimeUnit.SECONDS)).has(value("foo"));

		template.sendDefault(0, 2, "bar");
		ConsumerRecord<Integer, String> received = records.poll(10, TimeUnit.SECONDS);
		assertThat(received).has(key(2));
		assertThat(received).has(partition(0));
		assertThat(received).has(value("bar"));

		template.send(TOPIC, 0, 2, "baz");
		received = records.poll(10, TimeUnit.SECONDS);
		assertThat(received).has(key(2));
		assertThat(received).has(partition(0));
		assertThat(received).has(value("baz"));
	}
}
