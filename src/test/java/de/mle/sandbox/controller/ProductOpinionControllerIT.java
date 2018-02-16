package de.mle.sandbox.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;

import de.mle.sandbox.EmbeddedKafkaInitializer;
import de.mle.sandbox.domain.ProductOpinion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductOpinionControllerIT extends EmbeddedKafkaInitializer {
	private static final String SERVER = "http://localhost";
	private WebClient webClient;

	@LocalServerPort
	private int port;

	@Before
	public void setPort() {
		String serverWithPort = SERVER + ":" + port;
		webClient = WebClient.builder().baseUrl(serverWithPort).build();
	}

	@Test
	public void findProductOpinionById() {
		ProductOpinion savedOpinion = webClient
				.post().uri("/productOpinions")
				.syncBody(new ProductOpinion("name", "email", "subject", "rating", "comment"))
				.retrieve()
				.bodyToMono(ProductOpinion.class)
				.block();

		log.info("Saved opinion {}.", savedOpinion);

		ProductOpinion returnedOpinion = webClient
				.get().uri("/productOpinions/" + savedOpinion.getId())
				.retrieve()
				.bodyToMono(ProductOpinion.class)
				.block();

		log.info("Returned opinion {}.", returnedOpinion);
		assertThat(returnedOpinion.getId()).isEqualTo(savedOpinion.getId());
	}
}