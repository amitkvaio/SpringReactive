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

    @Test void exposesHealthAndMetric() {
        client.get().uri("/work").exchange().expectStatus().isOk();
        client.get().uri("/actuator/health").exchange().expectStatus().isOk();
        client.get().uri("/actuator/metrics/classroom.work.completed").exchange().expectStatus().isOk();
    }
    @Test void capacityIsReleasedOnCancellation() {
        CapacityController controller = new CapacityController(new io.micrometer.core.instrument.simple.SimpleMeterRegistry());
        for (int n = 0; n < 6; n++) reactor.test.StepVerifier.create(controller.work()).thenCancel().verify();
        reactor.test.StepVerifier.withVirtualTime(controller::work).thenAwait(java.time.Duration.ofSeconds(1))
            .expectNextCount(1).verifyComplete();
    }
    @Test void rejectsFifthConcurrentRequest() {
        CapacityController controller = new CapacityController(new io.micrometer.core.instrument.simple.SimpleMeterRegistry());
        java.util.List<reactor.core.Disposable> running = new java.util.ArrayList<>();
        try {
            reactor.test.StepVerifier.withVirtualTime(() -> {
                for (int n = 0; n < 4; n++) running.add(controller.work().subscribe());
                return controller.work();
            }).expectErrorSatisfies(e ->
                assertEquals(429, ((org.springframework.web.server.ResponseStatusException)e).getStatusCode().value())).verify();
        } finally { running.forEach(reactor.core.Disposable::dispose); }
    }
}
