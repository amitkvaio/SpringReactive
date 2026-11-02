package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.server.ResponseStatusException;
import io.micrometer.core.instrument.MeterRegistry;
@RestController
@RequestMapping("/orders")
public class OrderController {
    record Input(@NotBlank @Size(max=100) String product, @Positive int quantity) {}
    record StatusInput(@NotBlank String status) {}
    private final OrderService service;
    private final OrderRepository repository;
    private final DeliveryClient delivery;
    private final MeterRegistry metrics;
    public OrderController(OrderService service, OrderRepository repository, DeliveryClient delivery, MeterRegistry metrics) {
        this.service=service; this.repository=repository; this.delivery=delivery; this.metrics=metrics;
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) Mono<Order> create(@Valid @RequestBody Input input) {
        return service.create(input.product(), input.quantity()).doOnSuccess(order -> metrics.counter("classroom.orders.created").increment());
    }
    @GetMapping Flux<Order> list(@RequestParam(defaultValue="") String term,
            @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="5") int size) {
        if (page < 0 || size < 1 || size > 50) return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "page >= 0 and size 1..50 required"));
        return repository.page(term, size, (long) page * size);
    }
    @GetMapping("/{id}") Mono<Order> one(@PathVariable long id) { return service.find(id); }
    @PatchMapping("/{id}/status") Mono<Order> status(@PathVariable long id, @Valid @RequestBody StatusInput input) { return service.update(id, input.status()); }
    @GetMapping("/{id}/delivery") Mono<Map> delivery(@PathVariable long id) {
        return service.find(id).flatMap(order -> delivery.eta(id)
            .onErrorReturn(Map.of("orderId", id, "status", "ETA_PENDING")));
    }
    @GetMapping(value="/{id}/events", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    Mono<ResponseEntity<Flux<ServerSentEvent<Order>>>> events(@PathVariable long id) {
        // Check existence before committing SSE response headers so missing IDs still return HTTP 404.
        return service.find(id).map(order -> ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(
            service.events(id).map(value -> ServerSentEvent.<Order>builder(value).event("delivery").build())));
    }
}
