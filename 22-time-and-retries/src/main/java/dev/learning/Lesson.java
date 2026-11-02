package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson {
    static Mono<String> retryRead(AtomicInteger attempts) {
        return Mono.defer(() -> attempts.incrementAndGet() < 3
            ? Mono.<String>error(new IllegalStateException("temporary")) : Mono.just("delivery ready"))
            .retryWhen(Retry.backoff(2, Duration.ofMillis(100)).jitter(0)
                .filter(IllegalStateException.class::isInstance));
    }
    static Mono<String> deadline() { return Mono.<String>never().timeout(Duration.ofSeconds(1)); }
    static Flux<String> demo() { return retryRead(new AtomicInteger()).flux(); }
    public static void main(String[] args) {
        // Terminal boundary only: no HTTP request thread is blocked by this console demo.
        try {
            System.out.println("Demo: " + demo().collectList().block(Duration.ofSeconds(10)));
        } finally {
            Schedulers.shutdownNow();
        }
    }
}
