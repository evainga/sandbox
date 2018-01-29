package de.mle.sandbox.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import de.mle.sandbox.domain.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sender {
	@Autowired
	private KafkaTemplate<String, Product> kafkaTemplate;

	public void send(String topic, Product product) {
		kafkaTemplate.send(topic, product);
		log.info("Sent payload='{}' to topic='{}'", product, topic);
	}
}
