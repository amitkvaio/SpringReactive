package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void sameBusinessResult() {
        StepVerifier.create(Lesson.demo()).expectNextSequence(Lesson.imperative()).verifyComplete();
    }
    @Test void workStartsOnSubscription() {
        AtomicInteger calls = new AtomicInteger();
        Mono<String> lookup = Lesson.deferred(calls);
        assertEquals(0, calls.get());
        StepVerifier.create(lookup).expectNext("lookup-1").verifyComplete();
        StepVerifier.create(lookup).expectNext("lookup-2").verifyComplete();
    }
}
