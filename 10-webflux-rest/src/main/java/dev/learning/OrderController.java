package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Comparator;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.web.server.ResponseStatusException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/orders")
public class OrderController {
    record CreateOrder(@NotBlank String product, @Positive int quantity) {}
    record Order(long id, String product, int quantity) {}
    private final Map<Long, Order> orders = new ConcurrentHashMap<>(Map.of(1L, new Order(1, "tea", 2)));
    private final AtomicLong ids = new AtomicLong(1);
    @GetMapping public Flux<Order> all() { return Flux.defer(() -> Flux.fromIterable(orders.values())).sort(Comparator.comparingLong(Order::id)); }
    @GetMapping("/{id}") public Mono<Order> one(@PathVariable long id) {
        return Mono.defer(() -> Mono.justOrEmpty(orders.get(id)))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found")));
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> create(@Valid @RequestBody CreateOrder input) {
        return Mono.fromSupplier(() -> {
            Order order = new Order(ids.incrementAndGet(), input.product(), input.quantity());
            orders.put(order.id(), order);
            return order;
        });
    }
}
