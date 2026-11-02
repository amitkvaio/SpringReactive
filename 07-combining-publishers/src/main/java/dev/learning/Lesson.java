package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;

public class Lesson {
    static Mono<String> summary() {
        return Mono.zip(Mono.just("Asha"), Mono.just("order-1"), (customer, order) -> customer + ": " + order);
    }
    static Flux<String> merged() { return Flux.merge(Flux.just("packed"), Flux.just("shipped")); }
    static Flux<String> demo() { return Flux.concat(summary(), Mono.just("ready")); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
