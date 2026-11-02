package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void cleanupOnCompletion() {
        AtomicBoolean closed = new AtomicBoolean();
        StepVerifier.create(Lesson.usingResource(closed)).expectNext("read", "processed").verifyComplete();
        assertTrue(closed.get());
    }
    @Test void cleanupOnCancellation() {
        AtomicBoolean closed = new AtomicBoolean();
        StepVerifier.create(Lesson.usingResource(closed), 0).thenCancel().verify();
        assertTrue(closed.get());
    }
    @Test void cleanupOnError() {
        AtomicBoolean closed = new AtomicBoolean();
        StepVerifier.create(Flux.using(() -> "resource", r -> Mono.error(new IllegalStateException("failed")), r -> closed.set(true)))
            .verifyErrorMessage("failed");
        assertTrue(closed.get());
    }
    @Test void checkpointAddsAssemblyClue() {
        StepVerifier.create(Lesson.failed()).expectErrorSatisfies(error -> {
            assertEquals("bad order", error.getMessage());
            assertTrue(java.util.Arrays.stream(error.getSuppressed()).anyMatch(e -> e.toString().contains("order-lookup")));
        }).verify();
    }
}
