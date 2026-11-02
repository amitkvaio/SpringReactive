package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;

public class Lesson {
    static Mono<String> lookup(int id) {
        return Mono.delay(Duration.ofMillis((4 - id) * 20L)).map(tick -> "order-" + id);
    }
    static Flux<String> unordered() { return Flux.range(1, 3).flatMap(Lesson::lookup, 3); }
    static Flux<String> sequential() { return Flux.range(1, 3).concatMap(Lesson::lookup); }
    static Flux<String> orderedConcurrent() { return Flux.range(1, 3).flatMapSequential(Lesson::lookup, 3); }
    static Flux<String> demo() { return orderedConcurrent(); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
