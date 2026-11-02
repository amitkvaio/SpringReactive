package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson {
    static Flux<String> work(AtomicInteger active, AtomicInteger peak) {
        return Flux.range(1, 6).flatMap(id -> Mono.defer(() -> {
            peak.accumulateAndGet(active.incrementAndGet(), Math::max);
            return Mono.delay(Duration.ofMillis(20)).map(tick -> "order-" + id)
                .doOnTerminate(active::decrementAndGet);
        }).timeout(Duration.ofSeconds(1)).onErrorReturn("delivery pending"), 2);
    }
    static Flux<String> demo() { return work(new AtomicInteger(), new AtomicInteger()).sort(); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
