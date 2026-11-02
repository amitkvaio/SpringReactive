package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void coldSourceRunsForEachSubscriber() {
        AtomicInteger calls = new AtomicInteger();
        Mono<Integer> cold = Mono.fromSupplier(calls::incrementAndGet);
        StepVerifier.create(cold).expectNext(1).verifyComplete();
        StepVerifier.create(cold).expectNext(2).verifyComplete();
    }
    @Test void cacheReusesResult() {
        Mono<Integer> cached = Lesson.cached(new AtomicInteger());
        StepVerifier.create(cached).expectNext(1).verifyComplete();
        StepVerifier.create(cached).expectNext(1).verifyComplete();
    }
    @Test void lateSubscriberReceivesOnlyReplayLimit() {
        Sinks.Many<String> sink = Lesson.announcements();
        sink.tryEmitNext("created").orThrow(); sink.tryEmitNext("packed").orThrow(); sink.tryEmitNext("shipped").orThrow();
        sink.tryEmitComplete().orThrow();
        StepVerifier.create(sink.asFlux()).expectNext("packed", "shipped").verifyComplete();
    }
    @Test void shareUsesOneLiveSubscription() {
        AtomicInteger subscriptions = new AtomicInteger();
        Flux<Integer> shared = Flux.<Integer>never().doOnSubscribe(s -> subscriptions.incrementAndGet()).share();
        reactor.core.Disposable first = shared.subscribe();
        reactor.core.Disposable second = shared.subscribe();
        assertEquals(1, subscriptions.get());
        first.dispose(); second.dispose();
    }
}
