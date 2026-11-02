package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void keepsAffordablePricesAndLabelsThem() {
        StepVerifier.create(Lesson.labels()).expectNext("Price: 10", "Price: 25").verifyComplete();
    }
    @Test void emptyInputStaysEmpty() {
        StepVerifier.create(Flux.<Integer>empty().filter(p -> p <= 25).map(p -> "Price: " + p)).verifyComplete();
    }
    @Test void mapperFailureTerminatesSequence() {
        StepVerifier.create(Flux.just(0).map(p -> 10 / p)).verifyError(ArithmeticException.class);
    }
}
