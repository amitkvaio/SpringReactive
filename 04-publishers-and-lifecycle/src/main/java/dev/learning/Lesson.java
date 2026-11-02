package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson {
    static Mono<Integer> fresh(AtomicInteger counter) {
        return Mono.defer(() -> Mono.just(counter.incrementAndGet()));
    }
    static Mono<String> nullable() { return Mono.justOrEmpty((String) null); }
    static Flux<String> demo() {
        return Flux.fromIterable(List.of("subscribe", "next", "complete"));
    }
    static Flux<Long> ticking(AtomicBoolean cancelled) {
        return Flux.interval(Duration.ofSeconds(1)).doOnCancel(() -> cancelled.set(true));
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
