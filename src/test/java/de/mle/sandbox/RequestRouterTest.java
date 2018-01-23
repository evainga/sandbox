package de.mle.sandbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import de.mle.sandbox.handler.PostHandler;

public class RequestRouterTest {
	private RequestRouter router = new RequestRouter();

	@Test
	public void routes() {
		// when
		RouterFunction<ServerResponse> routes = router.routes(mock(PostHandler.class));

		// then
		assertThat(routes).isNotNull();
	}
}
