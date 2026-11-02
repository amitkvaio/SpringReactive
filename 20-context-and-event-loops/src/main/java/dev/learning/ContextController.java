package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;

import reactor.core.scheduler.Schedulers;
@RestController
public class ContextController {
    @GetMapping("/context") Mono<Map<String,String>> context() {
        return Mono.just("start").publishOn(Schedulers.parallel()).flatMap(value -> Mono.deferContextual(context ->
            Mono.just(Map.of("requestId", context.get(CorrelationFilter.KEY), "thread", Thread.currentThread().getName()))));
    }
}
