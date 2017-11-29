package de.mle.sandbox;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.testng.annotations.Test;

import sandbox.SandboxApplication;

/**
 * Initial showcase of the new reactive clients {@code WebClient} and
 * {@code WebTestClient}
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { SandboxApplication.class })
public class WebClientIT extends AbstractTestNGSpringContextTests {
	private static final String BODY_CONTENT = "Hello World";
	private static final String SERVER = "http://localhost:8080";
	private static final WebTestClient WEB_TEST_CLIENT = WebTestClient.bindToServer().baseUrl(SERVER).build();
	private static final WebClient WEB_CLIENT = WebClient.builder().baseUrl(SERVER).build();

	@Test(enabled = false)
	public void makeRequestWithWebTestClient() {
		WEB_TEST_CLIENT
				.get().uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectBody().consumeWith(resp -> assertThat(resp.toString()).contains(BODY_CONTENT));
	}

	@Test(enabled = false)
	public void withRetrieveAndBlockMethod() {
		String body = WEB_CLIENT
				.get().uri("/")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		assertThat(body).contains(BODY_CONTENT);
	}

	@Test(enabled = false)
	public void withExchangeMethod() {
		String body = WEB_CLIENT
				.get().uri("/")
				.exchange()
				.flatMap(resp -> resp.bodyToMono(String.class))
				.block();
		assertThat(body).contains(BODY_CONTENT);
	}
}
