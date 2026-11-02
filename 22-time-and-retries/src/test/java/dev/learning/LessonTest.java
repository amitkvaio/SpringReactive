package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void retriesTwiceThenSucceeds() {
        AtomicInteger attempts = new AtomicInteger();
        StepVerifier.withVirtualTime(() -> Lesson.retryRead(attempts)).thenAwait(Duration.ofSeconds(1))
            .expectNext("delivery ready").verifyComplete();
        assertEquals(3, attempts.get());
    }
    @Test void timeoutTerminatesStalledRead() {
        StepVerifier.withVirtualTime(Lesson::deadline).thenAwait(Duration.ofSeconds(1))
            .verifyError(java.util.concurrent.TimeoutException.class);
    }
    @Test void permanentErrorsAreNotRetried() {
        AtomicInteger calls = new AtomicInteger();
        StepVerifier.create(Mono.defer(() -> { calls.incrementAndGet(); return Mono.error(new IllegalArgumentException("invalid")); })
            .retryWhen(reactor.util.retry.Retry.max(2).filter(IllegalStateException.class::isInstance)))
            .verifyErrorMessage("invalid");
        assertEquals(1, calls.get());
    }
}
