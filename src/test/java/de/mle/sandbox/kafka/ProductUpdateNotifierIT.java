package de.mle.sandbox.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.mle.sandbox.EmbeddedKafkaInitializer;
import de.mle.sandbox.domain.Product;
import de.mle.sandbox.kafka.consumer.Receiver;
import de.mle.sandbox.kafka.producer.Sender;

@SpringBootTest
public class ProductUpdateNotifierIT extends EmbeddedKafkaInitializer {
	private static final String TOPIC = "helloworld.t";

	@Autowired
	private Receiver receiver;

	@Autowired
	private Sender sender;

	@Test
	public void testReceive() throws Exception {
		sender.send(TOPIC, new Product(new Random().nextLong(), "Hello Spring Kafka!"));

		receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		assertThat(receiver.getLatch().getCount()).isEqualTo(0);
	}
}
