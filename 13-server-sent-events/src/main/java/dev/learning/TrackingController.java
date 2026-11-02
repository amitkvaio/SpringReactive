package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import java.time.Duration;

import java.util.concurrent.atomic.AtomicInteger;
@RestController
public class TrackingController {
    final AtomicInteger cancellations = new AtomicInteger();
    Flux<ServerSentEvent<String>> events() {
        return Flux.just("CREATED", "PACKED", "DELIVERED").delayElements(Duration.ofSeconds(1))
            .index().map(pair -> ServerSentEvent.<String>builder(pair.getT2()).id(Long.toString(pair.getT1())).event("delivery").build())
            .doOnCancel(cancellations::incrementAndGet);
    }
    @GetMapping(value="/events", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<ServerSentEvent<String>> track() { return events(); }
}
