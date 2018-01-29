package de.mle.sandbox.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import de.mle.sandbox.EmbeddedKafkaInitializer;
import de.mle.sandbox.logging.MemoryAppender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductUpdateNotifierControllerIT extends EmbeddedKafkaInitializer {
	@LocalServerPort
	private int port;

	@Test
	public void notifyUpdate() {
		// given
		long productId = new Random().nextLong();
		String expectedLogMessage = "Sent payload='Product(id=" + productId + ", title=n/a)' to topic='productUpdateNotification'";

		// when
		RestAssured.given()
				.baseUri("http://localhost:" + port)
				.pathParam("productId", productId)
				.when()
				.get("/notifyProductUpdate/{productId}")
				.then()
				.statusCode(HttpStatus.NO_CONTENT.value());

		// then
		List<ILoggingEvent> loggingEventsWithCorrectId = MemoryAppender.LOG_MESSAGES.stream()
				.filter(loggingEvent -> loggingEvent.getLevel() == Level.INFO)
				.filter(loggingEvent -> loggingEvent.getFormattedMessage().equals(expectedLogMessage))
				.collect(Collectors.toList());

		assertThat(loggingEventsWithCorrectId).hasSize(1);
	}
}
