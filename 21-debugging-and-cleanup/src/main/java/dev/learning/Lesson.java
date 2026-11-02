package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class Lesson {
    static Flux<String> usingResource(AtomicBoolean closed) {
        return Flux.using(() -> "borrowed-resource", resource -> Flux.just("read", "processed"),
            resource -> closed.set(true));
    }
    static Mono<String> failed() {
        return Mono.<String>error(new IllegalStateException("bad order")).checkpoint("order-lookup");
    }
    static Flux<String> demo() { return usingResource(new AtomicBoolean()); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
