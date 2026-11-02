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

    @Test void carriesRequestIdAcrossScheduler() {
        client.get().uri("/context").header("X-Request-Id", "class-42").exchange().expectStatus().isOk()
            .expectHeader().valueEquals("X-Request-Id", "class-42").expectBody().jsonPath("$.requestId").isEqualTo("class-42");
    }
    @Test void generatesIdWhenMissing() {
        client.get().uri("/context").exchange().expectStatus().isOk().expectHeader().exists("X-Request-Id");
    }
}
