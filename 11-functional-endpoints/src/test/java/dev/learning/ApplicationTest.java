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

    @Test void routesToHandler() { client.get().uri("/products").exchange().expectStatus().isOk().expectBody().jsonPath("$[0].name").isEqualTo("tea"); }
    @Test void unknownRouteIs404() { client.get().uri("/unknown").exchange().expectStatus().isNotFound(); }
}
