package dev.learning;
import reactor.core.publisher.Mono;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
@Controller
public class GreetingController {
    @MessageMapping("greet") Mono<String> greet(@Payload String name) { return Mono.just("hello " + name); }
}
