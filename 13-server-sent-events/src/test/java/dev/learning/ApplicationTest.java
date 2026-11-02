package dev.learning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"DEMO_PASSWORD=test-only", "DEMO_USER=student"})
class ApplicationTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Autowired Environment environment;
    WebTestClient client;
    @BeforeEach void client() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + environment.getProperty("local.server.port")).build();
    }

    @Test void streamsSse() {
        var events = client.get().uri("/events").accept(org.springframework.http.MediaType.TEXT_EVENT_STREAM)
            .exchange().expectStatus().isOk().returnResult(String.class).getResponseBody();
        reactor.test.StepVerifier.create(events).expectNext("CREATED", "PACKED", "DELIVERED").verifyComplete();
    }
    @Test void cancellationStopsTimerChain() {
        TrackingController controller = new TrackingController();
        reactor.test.StepVerifier.withVirtualTime(controller::events).thenAwait(java.time.Duration.ofSeconds(1))
            .expectNextCount(1).thenCancel().verify();
        assertEquals(1, controller.cancellations.get());
    }
}
