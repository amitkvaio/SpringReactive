package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class LessonTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void oneReceipt() { StepVerifier.create(Lesson.receipt(1)).expectNext("receipt-1").verifyComplete(); }
    @Test void missingReceipt() { StepVerifier.create(Lesson.receipt(9)).verifyComplete(); }
    @Test void failedReceipt() { StepVerifier.create(Lesson.failedReceipt()).verifyErrorMessage("shop closed"); }
    @Test void severalProducts() { StepVerifier.create(Lesson.demo()).expectNext("tea", "coffee", "juice").verifyComplete(); }
}
