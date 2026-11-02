package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void publisherRespectsDemand() {
        StepVerifier.create(Lesson.demo(), 0).thenRequest(2).expectNext(1, 2)
            .thenRequest(3).expectNext(3, 4, 5).verifyComplete();
    }
    @Test void manualSubscriberReceivesOnlyRequestedItems() {
        assertEquals(java.util.List.of(1, 2), Lesson.requestTwo());
    }
    @Test void subscriberCanCancel() {
        StepVerifier.create(Lesson.demo(), 0).thenRequest(1).expectNext(1).thenCancel().verify();
    }
}
