package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson {
    static Mono<Integer> cached(AtomicInteger calls) {
        return Mono.fromSupplier(calls::incrementAndGet).cache();
    }
    static Sinks.Many<String> announcements() { return Sinks.many().replay().limit(2); }
    static Flux<String> demo() {
        return Flux.defer(() -> {
            Sinks.Many<String> sink = announcements();
            sink.tryEmitNext("packed").orThrow();
            sink.tryEmitNext("shipped").orThrow();
            sink.tryEmitComplete().orThrow();
            return sink.asFlux();
        });
    }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
