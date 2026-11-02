package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson {
    static List<String> imperative() {
        List<String> receipts = new ArrayList<>();
        for (int id : List.of(1, 2, 3)) receipts.add("order-" + id);
        return receipts;
    }
    static Flux<String> demo() {
        return Flux.range(1, 3).map(id -> "order-" + id);
    }
    static Mono<String> deferred(AtomicInteger calls) {
        return Mono.fromSupplier(() -> "lookup-" + calls.incrementAndGet());
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
