package de.mle.sandbox;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Initial showcase of the new reactive clients {@code WebClient} and
 * {@code WebTestClient}
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebClientIT extends EmbeddedKafkaInitializer {
	private static final String BODY_CONTENT = "Welcome to the sandbox";
	private static final String SERVER = "http://localhost";

	private WebTestClient webTestClient;
	private WebClient webClient;

	@LocalServerPort
	private int port;

	@Before
	public void setPort() {
		String serverWithPort = SERVER + ":" + port;
		webTestClient = WebTestClient.bindToServer().baseUrl(serverWithPort).build();
		webClient = WebClient.builder().baseUrl(serverWithPort).build();
	}

	@Test
	public void makeRequestWithWebTestClient() {
		webTestClient
				.get().uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectBody().consumeWith(resp -> assertThat(resp.toString()).contains(BODY_CONTENT));
	}

	@Test
	public void withRetrieveAndBlockMethod() {
		String body = webClient
				.get().uri("/")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		assertThat(body).contains(BODY_CONTENT);
	}

	@Test
	public void withExchangeMethod() {
		String body = webClient
				.get().uri("/")
				.exchange()
				.flatMap(resp -> resp.bodyToMono(String.class))
				.block();
		assertThat(body).contains(BODY_CONTENT);
	}
}
