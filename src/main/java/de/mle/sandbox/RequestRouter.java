package de.mle.sandbox;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import de.mle.sandbox.handler.EchoHandler;
import de.mle.sandbox.handler.PostHandler;

@Configuration
public class RequestRouter {
	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(EchoHandler echoHandler) {
		return route(POST("/echo"), echoHandler::echo);
	}

	@Bean
	public RouterFunction<ServerResponse> routes(PostHandler postController) {
		return route(GET("/posts"), postController::all)
				.andRoute(POST("/posts"), postController::create)
				.andRoute(GET("/posts/{id}"), postController::get)
				.andRoute(PUT("/posts/{id}"), postController::update)
				.andRoute(DELETE("/posts/{id}"), postController::delete);
	}
}
