package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
@RestController
public class DeliveryController {
    private final DeliveryClient client;
    public DeliveryController(WebClient.Builder builder, @Value("${delivery.base-url:http://localhost:8082}") String baseUrl) {
        client = new DeliveryClient(builder.baseUrl(baseUrl).build());
    }
    @GetMapping("/delivery/{id}") Mono<String> eta(@PathVariable String id) {
        return client.eta(id).onErrorResume(IllegalArgumentException.class, e -> Mono.just("unknown delivery"))
            .onErrorResume(java.util.concurrent.TimeoutException.class, e -> Mono.just("ETA pending"));
    }
    @GetMapping("/stub/delivery/{id}") Mono<String> stub(@PathVariable String id) {
        if (id.equals("missing")) return Mono.error(new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND));
        return Mono.delay(id.equals("slow") ? Duration.ofSeconds(3) : Duration.ZERO).thenReturn("ETA: 15 minutes");
    }
}
