package sandbox;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RequestRouter {
	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(EchoHandler echoHandler) {
		return route(POST("/echo"), echoHandler::echo);
	}
}
