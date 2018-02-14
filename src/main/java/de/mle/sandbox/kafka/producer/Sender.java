package de.mle.sandbox.kafka.producer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import de.mle.sandbox.domain.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sender {
	@Autowired
	private KafkaTemplate<String, Product> kafkaTemplate;

	public void send(String topic, Product product) {
		try {
			kafkaTemplate.send(topic, product).get(2, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.warn("Error updating product {} within 2 seconds!", product, e);
			return;
		}
		log.info("Sent payload='{}' to topic='{}'", product, topic);
	}
}
