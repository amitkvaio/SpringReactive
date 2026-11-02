package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void freshValuePerSubscription() {
        AtomicInteger counter = new AtomicInteger();
        Mono<Integer> source = Lesson.fresh(counter);
        assertEquals(0, counter.get());
        StepVerifier.create(source).expectNext(1).verifyComplete();
        StepVerifier.create(source).expectNext(2).verifyComplete();
    }
    @Test void nullableInputBecomesEmpty() { StepVerifier.create(Lesson.nullable()).verifyComplete(); }
    @Test void cancellationRunsCleanupHook() {
        AtomicBoolean cancelled = new AtomicBoolean();
        StepVerifier.withVirtualTime(() -> Lesson.ticking(cancelled)).thenAwait(Duration.ofSeconds(1))
            .expectNext(0L).thenCancel().verify();
        assertTrue(cancelled.get());
    }
}
