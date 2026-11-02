package dev.learning;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Lesson {
    static Flux<Integer> demo() { return Flux.range(1, 5); }
    static List<Integer> requestTwo() {
        List<Integer> received = new ArrayList<>();
        demo().subscribe(new BaseSubscriber<Integer>() {
            @Override protected void hookOnSubscribe(org.reactivestreams.Subscription subscription) {
                request(2);
            }
            @Override protected void hookOnNext(Integer value) { received.add(value); }
        });
        return received;
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
