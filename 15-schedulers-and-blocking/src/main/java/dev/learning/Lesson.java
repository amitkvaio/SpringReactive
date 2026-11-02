package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;

public class Lesson {
    static Mono<String> legacyThread() {
        return Mono.fromCallable(() -> {
            // Classpath resource reads are blocking even when the file is small.
            try (var input = Lesson.class.getResourceAsStream("/legacy-receipt.txt")) {
                if (input == null) throw new IllegalStateException("legacy receipt is missing");
                String receipt = new String(input.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8).trim();
                return receipt + " on " + Thread.currentThread().getName();
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
    static Mono<String> downstreamThread() {
        return Mono.just("label").publishOn(Schedulers.parallel())
            .map(value -> value + " on " + Thread.currentThread().getName());
    }
    static Flux<String> demo() { return Flux.concat(legacyThread(), downstreamThread()); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
