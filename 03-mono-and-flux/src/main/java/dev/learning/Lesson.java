package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;

public class Lesson {
    static Mono<String> receipt(int id) { return id == 1 ? Mono.just("receipt-1") : Mono.empty(); }
    static Mono<String> failedReceipt() { return Mono.error(new IllegalStateException("shop closed")); }
    static Flux<String> demo() { return Flux.just("tea", "coffee", "juice"); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
