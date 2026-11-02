package dev.learning;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {
    @Autowired Environment environment;

    @Test void applicationStartsWithConfiguredHttpClient() {
        WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + environment.getProperty("local.server.port")).build()
            .get().uri("/stub/delivery/1").exchange().expectStatus().isOk()
            .expectBody(String.class).isEqualTo("ETA: 15 minutes");
    }
}
