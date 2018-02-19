package de.mle.sandbox.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import de.mle.sandbox.EmbeddedKafkaInitializer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductOpinionGuiControllerIT extends EmbeddedKafkaInitializer {
	private static final String SERVER = "http://localhost";

	private WebTestClient webTestClient;

	@LocalServerPort
	private int port;

	@Before
	public void setPort() {
		webTestClient = WebTestClient.bindToServer().baseUrl(SERVER + ":" + port).build();
	}

	@Test
	public void makeRequestWithWebTestClient() {
		webTestClient
				.get().uri("/productOpinions/gui")
				.exchange()
				.expectStatus().isOk()
				.expectBody().consumeWith(resp -> assertThat(resp.toString()).contains("Product opinions"));
	}
}
