package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/orders")
public class OrderController {
    record Input(@NotBlank @Size(max=100) String product, @Positive int quantity) {}
    private final OrderRepository orders;
    private final OrderService service;
    public OrderController(OrderRepository orders, OrderService service) { this.orders = orders; this.service = service; }
    @GetMapping Flux<Order> all(@RequestParam(defaultValue="") String term,
            @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="5") int size) {
        if (page < 0 || size < 1 || size > 50) return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "page >= 0 and size 1..50 required"));
        return orders.page(term, size, (long) page * size);
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    Mono<Order> create(@Valid @RequestBody Input input) { return service.create(input.product(), input.quantity()); }
}
