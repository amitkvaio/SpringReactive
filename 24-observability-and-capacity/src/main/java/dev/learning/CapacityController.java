package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.Map;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.Semaphore;
import org.springframework.web.server.ResponseStatusException;
@RestController
public class CapacityController {
    private final Semaphore permits = new Semaphore(4);
    private final Counter completed;
    public CapacityController(MeterRegistry registry) {
        completed = registry.counter("classroom.work.completed");
        Gauge.builder("classroom.work.available", permits, Semaphore::availablePermits).register(registry);
    }
    Mono<Map<String,String>> work() {
        return Mono.defer(() -> {
            if (!permits.tryAcquire()) return Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "capacity reached"));
            return Mono.delay(Duration.ofMillis(100)).map(t -> Map.of("status", "done"))
                .doOnSuccess(value -> completed.increment()).doFinally(signal -> permits.release());
        });
    }
    @GetMapping("/work") Mono<Map<String,String>> endpoint() { return work(); }
}
