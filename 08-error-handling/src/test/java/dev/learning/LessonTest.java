package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void replacesFailedSource() { StepVerifier.create(Lesson.fallback()).expectNext("collect in store").verifyComplete(); }
    @Test void translatesErrorAndRetainsCause() {
        StepVerifier.create(Lesson.translated()).expectErrorSatisfies(e -> {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertInstanceOf(IllegalStateException.class, e.getCause());
        }).verify();
    }
    @Test void fallbackDoesNotResumeFailedUpstream() {
        StepVerifier.create(Flux.just(1, 0, 2).map(n -> 10 / n).onErrorReturn(-1))
            .expectNext(10, -1).verifyComplete();
    }
}
