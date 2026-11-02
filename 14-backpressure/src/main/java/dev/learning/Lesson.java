package dev.learning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson {
    static Flux<Integer> demo() { return Flux.range(1, 6).limitRate(2); }
    static Flux<Integer> buffered(Flux<Integer> source) { return source.onBackpressureBuffer(2); }
    static Flux<Integer> dropped(AtomicInteger count) {
        return Flux.<Integer>create(sink -> { for (int n = 1; n <= 5; n++) sink.next(n); sink.complete(); },
            FluxSink.OverflowStrategy.IGNORE).onBackpressureDrop(n -> count.incrementAndGet());
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
