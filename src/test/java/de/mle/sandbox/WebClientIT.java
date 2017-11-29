package de.mle.sandbox;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.testng.annotations.Test;

/**
 * Initial showcase of the new reactive clients {@code WebClient} and {@code WebTestClient}
 */
public class WebClientIT {
    private static final String IDEALO_TITLE = "IDEALO &ndash; die Nr. 1 im Preisvergleich";
    private static final WebTestClient WEB_TEST_CLIENT = WebTestClient.bindToServer().baseUrl("https://www.idealo.de").build();
    private static final WebClient WEB_CLIENT = WebClient.builder().baseUrl("https://www.idealo.de").build();

    @Test
    public void makeRequestWithWebTestClient() {
        WEB_TEST_CLIENT
                .get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(resp -> assertThat(resp.toString()).contains(IDEALO_TITLE));
    }

    @Test
    public void withRetrieveAndBlockMethod() {
        String body = WEB_CLIENT
                .get().uri("/")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        assertThat(body).contains(IDEALO_TITLE);
    }

    @Test
    public void withExchangeMethod() {
        String body = WEB_CLIENT
                .get().uri("/")
                .exchange()
                .flatMap(resp -> resp.bodyToMono(String.class))
                .block();
        assertThat(body).contains(IDEALO_TITLE);
    }
}
