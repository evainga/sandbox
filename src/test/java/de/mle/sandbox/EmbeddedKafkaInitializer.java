package de.mle.sandbox;

import org.junit.runner.RunWith;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Because {code de.mle.sandbox.kafka.consumer.ReceiverConfig.bootstrapServers}
 * needs {@code kafka.bootstrap-servers} all integration tests have to have at
 * least theoretically the chance to gain access to the embedded Kafka.
 */
@DirtiesContext
@RunWith(SpringRunner.class)
@EmbeddedKafka(topics = { "helloworld.t" }, partitions = 1)
public class EmbeddedKafkaInitializer {
}
