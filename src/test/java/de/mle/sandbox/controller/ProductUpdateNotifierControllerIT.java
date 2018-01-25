package de.mle.sandbox.controller;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import de.mle.sandbox.EmbeddedKafkaInitializer;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductUpdateNotifierControllerIT extends EmbeddedKafkaInitializer {
	@LocalServerPort
	private int port;

	@Test
	public void notifyUpdate() {
		RestAssured.given()
				.baseUri("http://localhost:" + port)
				.pathParam("productId", 123)
				.when()
				.get("/notifyProductUpdate/{productId}")
				.then()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}
}
