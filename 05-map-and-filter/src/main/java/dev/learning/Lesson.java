package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;

public class Lesson {
    static Flux<String> labels() {
        return Flux.just(10, 25, 40).filter(price -> price <= 25).map(price -> "Price: " + price);
    }
    static Flux<String> demo() { return labels(); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
