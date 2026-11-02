package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;

public class Lesson {
    static Flux<Integer> demo() { return Flux.range(1, 4).filter(n -> n % 2 == 0); }
    static Mono<String> invalid() { return Mono.error(new IllegalArgumentException("invalid order")); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
