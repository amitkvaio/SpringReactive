package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import java.time.Duration;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void valuesAndCompletion() { StepVerifier.create(Lesson.demo()).expectNext(2, 4).verifyComplete(); }
    @Test void errorMessage() { StepVerifier.create(Lesson.invalid()).verifyErrorMessage("invalid order"); }
    @Test void empty() { StepVerifier.create(Mono.empty()).verifyComplete(); }
    @Test void demandAndCancellation() {
        StepVerifier.create(Lesson.demo(), 0).thenRequest(1).expectNext(2).thenCancel().verify();
    }
    @Test void timeWithoutWaiting() {
        StepVerifier.withVirtualTime(() -> Mono.delay(Duration.ofHours(1)))
            .expectSubscription().expectNoEvent(Duration.ofMinutes(59))
            .thenAwait(Duration.ofMinutes(1)).expectNext(0L).verifyComplete();
    }
}
