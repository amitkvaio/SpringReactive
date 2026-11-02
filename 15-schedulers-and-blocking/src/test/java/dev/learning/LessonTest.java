package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void callableRunsOnBoundedElastic() {
        StepVerifier.create(Lesson.legacyThread()).assertNext(name -> assertTrue(name.contains("boundedElastic"))).verifyComplete();
    }
    @Test void downstreamRunsOnParallelScheduler() {
        StepVerifier.create(Lesson.downstreamThread()).assertNext(name -> assertTrue(name.contains("parallel"))).verifyComplete();
    }
    @Test void callableExceptionsAreSignals() {
        StepVerifier.create(Mono.fromCallable(() -> { throw new IllegalStateException("legacy failed"); })
            .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())).verifyErrorMessage("legacy failed");
    }
}
