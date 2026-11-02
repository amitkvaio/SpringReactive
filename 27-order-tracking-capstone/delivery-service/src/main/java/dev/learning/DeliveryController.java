package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
public class DeliveryController {
    @GetMapping("/delivery/{id}") Mono<Map<String,Object>> eta(@PathVariable long id) {
        return Mono.just(Map.of("orderId", id, "etaMinutes", 15, "provider", "local-stub"));
    }
}
