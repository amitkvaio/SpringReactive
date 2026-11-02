package dev.learning;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
@Component
public class DeliveryClient {
    private final WebClient client;
    public DeliveryClient(WebClient.Builder builder, @Value("${delivery.base-url:http://localhost:8092}") String baseUrl) {
        client = builder.baseUrl(baseUrl).build();
    }
    public Mono<Map> eta(long id) {
        return Mono.deferContextual(context -> client.get().uri("/delivery/{id}", id)
            .header("X-Request-Id", context.getOrDefault(CorrelationFilter.KEY, "local"))
            .retrieve().bodyToMono(Map.class))
            .timeout(Duration.ofSeconds(1))
            .retryWhen(Retry.backoff(2, Duration.ofMillis(100)).filter(error ->
                error instanceof java.util.concurrent.TimeoutException || error instanceof WebClientRequestException ||
                error instanceof WebClientResponseException response && response.getStatusCode().is5xxServerError()))
            .timeout(Duration.ofSeconds(4));
    }
}
