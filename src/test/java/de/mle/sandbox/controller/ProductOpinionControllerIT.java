package de.mle.sandbox.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
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
		// given
		ProductOpinion savedOpinion = createProductOpinion();

		// when
		ProductOpinion returnedOpinion = webClient
				.get().uri("/productOpinions/" + savedOpinion.getId())
				.retrieve()
				.bodyToMono(ProductOpinion.class)
				.block();

		// then
		assertThat(returnedOpinion.getId()).isEqualTo(savedOpinion.getId());
	}

	@Test
	public void patchExistingProductOpinion() {
		// given
		ProductOpinion savedOpinion = createProductOpinion();
		String newName = "new name";

		// when
		ProductOpinion patchedOpinion = webClient
				.patch().uri("/productOpinions/" + savedOpinion.getId())
				.syncBody(Maps.newHashMap("name", newName))
				.retrieve()
				.bodyToMono(ProductOpinion.class)
				.block();

		// then
		assertThat(patchedOpinion.getName()).isEqualTo(newName);
	}

	@Test
	public void deleteExistingProductOpinion() {
		// given
		ProductOpinion savedOpinion = createProductOpinion();

		// when
		HttpStatus deleteStatusCode = webClient
				.delete().uri("/productOpinions/" + savedOpinion.getId())
				.exchange()
				.block().statusCode();

		// then
		assertThat(deleteStatusCode).isEqualTo(HttpStatus.NO_CONTENT);

		HttpStatus getStatusCode = webClient
				.get().uri("/productOpinions/" + savedOpinion.getId())
				.exchange()
				.block().statusCode();

		assertThat(getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void deleteNonExistingProductOpinion() {
		// when
		HttpStatus statusCode = webClient
				.delete().uri("/productOpinions/1234567")
				.exchange()
				.block().statusCode();

		// then
		assertThat(statusCode).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private ProductOpinion createProductOpinion() {
		ProductOpinion savedOpinion = webClient
				.post().uri("/productOpinions")
				.syncBody(new ProductOpinion("name", "email", "subject", "rating", "comment"))
				.retrieve()
				.bodyToMono(ProductOpinion.class)
				.block();
		return savedOpinion;
	}
}