package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void obeysDownstreamDemand() {
        StepVerifier.create(Lesson.demo(), 0).thenRequest(2).expectNext(1, 2)
            .thenRequest(4).expectNext(3, 4, 5, 6).verifyComplete();
    }
    @Test void boundedBufferOverflows() {
        var source = reactor.test.publisher.TestPublisher.<Integer>create();
        StepVerifier.create(Lesson.buffered(source.flux()), 0)
            .then(() -> source.next(1, 2, 3)).thenRequest(2).expectNext(1, 2)
            .expectErrorMatches(reactor.core.Exceptions::isOverflow).verify();
    }
    @Test void dropPolicyDiscardsWithoutDemand() {
        AtomicInteger dropped = new AtomicInteger();
        StepVerifier.create(Lesson.dropped(dropped), 0).verifyComplete();
        assertEquals(5, dropped.get());
    }
}
