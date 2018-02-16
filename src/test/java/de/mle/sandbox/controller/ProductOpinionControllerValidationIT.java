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
import de.mle.sandbox.domain.SpringValidationErrors;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductOpinionControllerValidationIT extends EmbeddedKafkaInitializer {
	private static final String SERVER = "http://localhost";
	private WebClient webClientWithBaseUrl;

	@LocalServerPort
	private int port;

	@Before
	public void setPort() {
		String serverWithPort = SERVER + ":" + port;
		webClientWithBaseUrl = WebClient.builder().baseUrl(serverWithPort).build();
	}

	@Test
	public void checkEMailValidation() {
		// when
		SpringValidationErrors errorResponse = postProductOpinionAndReturnErrorObject(
				new ProductOpinion("name", "email-invalid", "subject", 4, "comment", "127.0.0.1", "www.hostname.de"));

		// then
		assertThat(errorResponse.getErrors().get(0).getEntity()).isEqualTo("ProductOpinion");
		assertThat(errorResponse.getErrors().get(0).getProperty()).isEqualTo("email");
		assertThat(errorResponse.getErrors().get(0).getInvalidValue()).isEqualTo("email-invalid");
	}

	@Test
	public void checkRatingStarsValidation() {
		// when
		SpringValidationErrors errorResponse = postProductOpinionAndReturnErrorObject(
				new ProductOpinion("name", "email@mail.com", "subject", 40, "comment", "127.0.0.1", "www.hostname.de"));

		// then
		assertThat(errorResponse.getErrors().get(0).getEntity()).isEqualTo("ProductOpinion");
		assertThat(errorResponse.getErrors().get(0).getProperty()).isEqualTo("ratingCount");
		assertThat(errorResponse.getErrors().get(0).getInvalidValue()).isEqualTo("40");
	}

	@Test
	public void checkConditionalValidation() {
		// when
		SpringValidationErrors errorResponse = postProductOpinionAndReturnErrorObject(
				new ProductOpinion("name", "email@mail.com", "subject", 5, "good comment", "127.0.0.1", "www.hostname.de"));

		// then
		assertThat(errorResponse.getErrors().get(0).getEntity()).isEqualTo("ProductOpinion");
		assertThat(errorResponse.getErrors().get(0).getProperty()).isEqualTo("comment");
		assertThat(errorResponse.getErrors().get(0).getInvalidValue()).isEqualTo("good comment");
		assertThat(errorResponse.getErrors().get(0).getMessage()).isEqualTo("5 stars and a good comment do not fit together!");
	}

	private SpringValidationErrors postProductOpinionAndReturnErrorObject(ProductOpinion opinion) {
		SpringValidationErrors errorResponse = webClientWithBaseUrl
				.post().uri("/productOpinions")
				.syncBody(opinion)
				.exchange()
				.block()
				.bodyToMono(SpringValidationErrors.class)
				.block();
		return errorResponse;
	}

}