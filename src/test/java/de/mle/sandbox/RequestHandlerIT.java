package de.mle.sandbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

import java.net.URI;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import de.mle.sandbox.domain.Post;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RequestHandlerIT extends EmbeddedKafkaInitializer {
	@Autowired
	private RouterFunction<ServerResponse> routes;

	private WebTestClient client;

	@Before
	public void init() {
		client = WebTestClient
				.bindToRouterFunction(routes)
				.configureClient()
				.baseUrl("http://localhost:8080/")
				.build();
	}

	@Test
	public void getAllPostsShouldBeOkWithAuthetication() {
		client
				.get()
				.uri("/posts/")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void getANoneExistedPostShouldReturn404() {
		client
				.get()
				.uri("/posts/ABC")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void postCrudOperations() {
		int randomInt = new Random().nextInt();
		String title = "Post test " + randomInt;
		FluxExchangeResult<Void> postResult = client
				.mutate().filter(basicAuthentication("user", "password")).build()
				.post()
				.uri("/posts")
				.body(BodyInserters.fromObject(Post.builder().title(title).content("content of " + title).build()))
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.CREATED)
				.returnResult(Void.class);

		URI location = postResult.getResponseHeaders().getLocation();
		assertThat(location).isNotNull();

		EntityExchangeResult<byte[]> getResult = client
				.get()
				.uri(location)
				.exchange()
				.expectStatus().isOk()
				.expectBody().jsonPath("$.title").isEqualTo(title)
				.returnResult();

		String getPost = new String(getResult.getResponseBody());
		assertThat(getPost).contains(title);
	}

}
