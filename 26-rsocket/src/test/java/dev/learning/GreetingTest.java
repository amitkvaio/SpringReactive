package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class GreetingTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void respondsToName() { StepVerifier.create(new GreetingController().greet("student")).expectNext("hello student").verifyComplete(); }
}
