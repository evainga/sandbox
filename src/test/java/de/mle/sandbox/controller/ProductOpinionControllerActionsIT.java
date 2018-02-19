package de.mle.sandbox.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import de.mle.sandbox.EmbeddedKafkaInitializer;
import de.mle.sandbox.domain.ProductOpinion;
import de.mle.sandbox.domain.State;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductOpinionControllerActionsIT extends EmbeddedKafkaInitializer {
	private static final String SERVER = "http://localhost";
	private WebClient webClientWithBaseUrl;
	private WebClient webClient;

	@LocalServerPort
	private int port;

	@Before
	public void setPort() {
		String serverWithPort = SERVER + ":" + port;
		webClientWithBaseUrl = WebClient.builder().baseUrl(serverWithPort).build();
		webClient = WebClient.create();
	}

	@Test
	public void findProductOpinionById() {
		// given
		String newOpinionLocation = createProductOpinionAndReturnLocationHeader();

		// when
		ProductOpinion returnedOpinion = webClient
				.get().uri(newOpinionLocation)
				.retrieve()
				.bodyToMono(ProductOpinion.class)
				.block();

		// then
		assertThat(returnedOpinion.getName()).isEqualTo("name");
		assertThat(returnedOpinion.getRatingCount()).isEqualTo(3);
	}

	@Test
	public void patchExistingProductOpinion() {
		// given
		String newOpinionLocation = createProductOpinionAndReturnLocationHeader();
		String newName = "new name";

		// when
		ProductOpinion patchedOpinion = webClient
				.patch().uri(newOpinionLocation)
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
		String newOpinionLocation = createProductOpinionAndReturnLocationHeader();

		// when
		HttpStatus deleteStatusCode = webClient
				.delete().uri(newOpinionLocation)
				.exchange()
				.block().statusCode();

		// then
		assertThat(deleteStatusCode).isEqualTo(HttpStatus.NO_CONTENT);

		HttpStatus getStatusCode = webClient
				.get().uri(newOpinionLocation)
				.exchange()
				.block().statusCode();

		assertThat(getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void deleteNonExistingProductOpinion() {
		// when
		HttpStatus statusCode = webClientWithBaseUrl
				.delete().uri("/productOpinions/1234567")
				.exchange()
				.block().statusCode();

		// then
		assertThat(statusCode).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void approveProductOpinionpatchExistingProductOpinion() {
		// given
		String newOpinionLocation = createProductOpinionAndReturnLocationHeader();

		// when
		ProductOpinion patchedOpinion = webClient
				.patch().uri(newOpinionLocation)
				.syncBody(Maps.newHashMap("state", State.APPROVED))
				.retrieve()
				.bodyToMono(ProductOpinion.class)
				.block();

		// then
		assertThat(patchedOpinion.getState()).isEqualTo(State.APPROVED);
	}

	private String createProductOpinionAndReturnLocationHeader() {
		return webClientWithBaseUrl
				.post().uri("/productOpinions")
				.syncBody(new ProductOpinion("name", "email@mail.com", "subject", 3, "comment", "127.0.0.1", "www.hostname.de", State.NEW))
				.exchange()
				.block()
				.headers().header(HttpHeaders.LOCATION).get(0);
	}
}