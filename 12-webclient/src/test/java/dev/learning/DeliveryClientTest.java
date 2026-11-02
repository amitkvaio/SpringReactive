package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.assertTrue;

import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import org.springframework.web.reactive.function.client.WebClient;
class DeliveryClientTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void callsRealLocalStub() {
        DisposableServer server = HttpServer.create().port(0).route(routes -> routes
            .get("/stub/delivery/1", (req, res) -> res.sendString(Mono.just("ETA: 15 minutes")))
            .get("/stub/delivery/missing", (req, res) -> res.status(404).send())).bindNow();
        try {
            DeliveryClient client = new DeliveryClient(WebClient.create("http://localhost:" + server.port()));
            StepVerifier.create(client.eta("1")).expectNext("ETA: 15 minutes").verifyComplete();
            StepVerifier.create(client.eta("missing")).verifyErrorMessage("delivery not found");
        } finally { server.disposeNow(); }
    }
    @Test void stalledResponseTimesOutAndCancels() {
        AtomicBoolean cancelled = new AtomicBoolean();
        org.springframework.web.reactive.function.client.ExchangeFunction exchange = request ->
            Mono.<org.springframework.web.reactive.function.client.ClientResponse>never().doOnCancel(() -> cancelled.set(true));
        DeliveryClient client = new DeliveryClient(WebClient.builder().exchangeFunction(exchange).build());
        StepVerifier.withVirtualTime(() -> client.eta("slow")).thenAwait(Duration.ofSeconds(1))
            .verifyError(java.util.concurrent.TimeoutException.class);
        assertTrue(cancelled.get());
    }
}
