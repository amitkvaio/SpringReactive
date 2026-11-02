package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
public class PrivateController {
    @GetMapping("/private") Mono<Map<String,String>> message() { return Mono.just(Map.of("message", "hello student")); }
}
