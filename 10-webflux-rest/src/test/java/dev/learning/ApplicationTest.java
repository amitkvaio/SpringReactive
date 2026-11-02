package dev.learning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.reactive.server.WebTestClient;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"DEMO_PASSWORD=test-only", "DEMO_USER=student"})
class ApplicationTest {
    @Autowired Environment environment;
    WebTestClient client;
    @BeforeEach void client() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + environment.getProperty("local.server.port")).build();
    }

    @Test void findsSeedOrder() { client.get().uri("/orders/1").exchange().expectStatus().isOk().expectBody().jsonPath("$.product").isEqualTo("tea"); }
    @Test void missingOrderIs404() { client.get().uri("/orders/9999").exchange().expectStatus().isNotFound().expectBody().jsonPath("$.error").isEqualTo("order not found"); }
    @Test void rejectsInvalidInput() {
        client.post().uri("/orders").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(java.util.Map.of("product", "", "quantity", 0)).exchange().expectStatus().isBadRequest();
    }
    @Test void createsOrder() {
        client.post().uri("/orders").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(java.util.Map.of("product", "coffee", "quantity", 2)).exchange().expectStatus().isCreated()
            .expectBody().jsonPath("$.product").isEqualTo("coffee");
    }
}
