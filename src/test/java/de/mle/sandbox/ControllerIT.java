package de.mle.sandbox;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerIT extends EmbeddedKafkaInitializer {
	@Autowired
	private RouterFunction<ServerResponse> monoRouterFunction;
	@Autowired
	private WebTestClient webClient;

	private WebTestClient webClientWithRouterFunction;

	@Before
	public void init() {
		webClientWithRouterFunction = WebTestClient
				.bindToRouterFunction(monoRouterFunction)
				.configureClient()
				.baseUrl("http://localhost:8080/")
				.build();
	}

	@Test
	public void testWelcome() {
		webClient
				.get().uri("/")
				.accept(MediaType.TEXT_PLAIN)
				.exchange()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody()
				.contains("Welcome at the sandbox");
	}

	@Test
	public void testEcho() {
		webClientWithRouterFunction
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
				.get().uri("/actuator/health")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody().jsonPath("status", "UP");
	}

	@Test
	public void testMoreActuators() {
		webClient
				.get().uri("/actuator")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("_links.info").isMap()
				.jsonPath("_links.beans").isMap()
				.jsonPath("_links.loggers").isMap()
				.jsonPath("_links.env").isMap();
	}
}
