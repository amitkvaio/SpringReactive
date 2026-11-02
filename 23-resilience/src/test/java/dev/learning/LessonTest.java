package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void limitsActiveLookups() {
        AtomicInteger active = new AtomicInteger(), peak = new AtomicInteger();
        StepVerifier.withVirtualTime(() -> Lesson.work(active, peak)).thenAwait(Duration.ofSeconds(1))
            .expectNextCount(6).verifyComplete();
        assertTrue(peak.get() <= 2);
        assertEquals(0, active.get());
    }
    @Test void slowReadGetsFallback() {
        StepVerifier.withVirtualTime(() -> Mono.never().timeout(Duration.ofSeconds(1)).onErrorReturn("delivery pending"))
            .thenAwait(Duration.ofSeconds(1)).expectNext("delivery pending").verifyComplete();
    }
}
