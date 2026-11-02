package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import java.time.Duration;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void concatMapPreservesOrder() {
        StepVerifier.withVirtualTime(Lesson::sequential).thenAwait(Duration.ofSeconds(1))
            .expectNext("order-1", "order-2", "order-3").verifyComplete();
    }
    @Test void flatMapSequentialPreservesOutputOrder() {
        StepVerifier.withVirtualTime(Lesson::orderedConcurrent).thenAwait(Duration.ofSeconds(1))
            .expectNext("order-1", "order-2", "order-3").verifyComplete();
    }
    @Test void flatMapAllowsCompletionOrder() {
        StepVerifier.withVirtualTime(Lesson::unordered).thenAwait(Duration.ofSeconds(1))
            .expectNext("order-3", "order-2", "order-1").verifyComplete();
    }
}
