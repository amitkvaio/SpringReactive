package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;

public class Lesson {
    static Mono<String> remote() { return Mono.error(new IllegalStateException("delivery unavailable")); }
    static Mono<String> fallback() { return remote().onErrorResume(IllegalStateException.class, error -> Mono.just("collect in store")); }
    static Mono<String> translated() { return remote().onErrorMap(error -> new IllegalArgumentException("try later", error)); }
    static Flux<String> demo() { return fallback().flux(); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
