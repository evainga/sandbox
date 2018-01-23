package de.mle.sandbox.kafka.consumer;

import java.util.concurrent.CountDownLatch;

import org.springframework.kafka.annotation.KafkaListener;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Receiver {
	@Getter
	private final CountDownLatch latch = new CountDownLatch(1);

	@KafkaListener(topics = "${kafka.topic.helloworld}")
	public void receive(String payload) {
		log.info("received payload='{}'", payload);
		latch.countDown();
	}
}
