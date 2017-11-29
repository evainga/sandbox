package de.mle.sandbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.Test;

import reactor.core.publisher.Mono;
import sandbox.SandboxApplication;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { SandboxApplication.class })
public class SandboxApplicationIT extends AbstractTestNGSpringContextTests {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void testWelcome() {
		webClient
				.get().uri("/")
				.accept(MediaType.TEXT_PLAIN)
				.exchange()
				.expectBody(String.class)
				.isEqualTo("Hello World");
	}

	@Test
	public void testEcho() {
		webClient
				.post().uri("/echo")
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.TEXT_PLAIN)
				.body(Mono.just("Hello WebFlux!"), String.class)
				.exchange()
				.expectBody(String.class)
				.isEqualTo("Hello WebFlux!");
	}

	@Test
	public void testActuatorStatus() {
		webClient
				.get().uri("/application/status")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody().json("{\"status\":\"UP\"}");
	}
}
