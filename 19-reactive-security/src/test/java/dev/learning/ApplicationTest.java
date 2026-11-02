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

    @Test void requiresCredentials() { client.get().uri("/private").exchange().expectStatus().isUnauthorized(); }
    @Test void acceptsCredentials() {
        client.get().uri("/private").headers(h -> h.setBasicAuth("student", "test-only"))
            .exchange().expectStatus().isOk().expectBody().jsonPath("$.message").isEqualTo("hello student");
    }
    @Test void rejectsWrongPassword() {
        client.get().uri("/private").headers(h -> h.setBasicAuth("student", "wrong"))
            .exchange().expectStatus().isUnauthorized();
    }
}
