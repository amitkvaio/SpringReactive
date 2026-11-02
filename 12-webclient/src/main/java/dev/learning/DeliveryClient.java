package dev.learning;
import reactor.core.publisher.Mono;
import java.time.Duration;

import org.springframework.web.reactive.function.client.WebClient;
public class DeliveryClient {
    private final WebClient client;
    public DeliveryClient(WebClient client) { this.client = client; }
    public Mono<String> eta(String id) {
        return client.get().uri("/stub/delivery/{id}", id).retrieve()
            .onStatus(status -> status.value() == 404, response -> Mono.error(new IllegalArgumentException("delivery not found")))
            .bodyToMono(String.class).timeout(Duration.ofSeconds(1));
    }
}
