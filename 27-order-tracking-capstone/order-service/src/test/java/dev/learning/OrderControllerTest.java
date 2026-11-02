package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.test.web.reactive.server.WebTestClient;
class OrderControllerTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void missingOrderIs404BeforeStreamingStarts() {
        OrderService service = mock(OrderService.class);
        when(service.find(999L)).thenReturn(Mono.error(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "order not found")));
        var controller = new OrderController(service, mock(OrderRepository.class), mock(DeliveryClient.class), new io.micrometer.core.instrument.simple.SimpleMeterRegistry());
        WebTestClient.bindToController(controller).controllerAdvice(new ApiErrors()).build()
            .get().uri("/orders/999/events").exchange().expectStatus().isNotFound();
    }
    @Test void rejectsSkippedDeliveryStage() {
        OrderRepository repository = mock(OrderRepository.class);
        when(repository.findById(1L)).thenReturn(Mono.just(new Order(1L,"tea",2,"CREATED",0L)));
        var tx = mock(org.springframework.transaction.reactive.TransactionalOperator.class);
        when(tx.transactional(org.mockito.ArgumentMatchers.<Mono<Order>>any())).thenAnswer(call -> call.getArgument(0));
        StepVerifier.create(new OrderService(repository,mock(AuditRepository.class),tx).update(1L,"DELIVERED"))
            .expectErrorSatisfies(error -> assertEquals(409, ((org.springframework.web.server.ResponseStatusException)error).getStatusCode().value())).verify();
    }
    @Test void streamsOnlyChangedStatusesAndStopsAtDelivered() {
        OrderRepository repository = mock(OrderRepository.class);
        when(repository.findById(1L)).thenReturn(Mono.just(new Order(1L,"tea",2,"CREATED",0L)),
            Mono.just(new Order(1L,"tea",2,"CREATED",0L)),Mono.just(new Order(1L,"tea",2,"DELIVERED",1L)));
        OrderService service = new OrderService(repository,mock(AuditRepository.class),mock(org.springframework.transaction.reactive.TransactionalOperator.class));
        StepVerifier.withVirtualTime(() -> service.events(1L)).thenAwait(Duration.ofSeconds(3))
            .expectNextMatches(order -> order.status().equals("CREATED"))
            .expectNextMatches(order -> order.status().equals("DELIVERED")).verifyComplete();
    }
}
