package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void combinesCustomerAndOrder() {
        StepVerifier.create(Lesson.summary()).expectNext("Asha: order-1").verifyComplete();
    }
    @Test void concatKeepsSourceOrder() {
        StepVerifier.create(Lesson.demo()).expectNext("Asha: order-1", "ready").verifyComplete();
    }
    @Test void emptyZipHasNoPair() {
        StepVerifier.create(Mono.zip(Mono.just("Asha"), Mono.empty())).verifyComplete();
    }
    @Test void mergeContainsBothSources() {
        StepVerifier.create(Lesson.merged().collectSortedList()).expectNext(java.util.List.of("packed", "shipped")).verifyComplete();
    }
}
