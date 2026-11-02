package dev.learning;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.List;
@Service
public class OrderService {
    private final OrderRepository orders;
    private final AuditRepository audits;
    private final TransactionalOperator transaction;
    public OrderService(OrderRepository orders, AuditRepository audits, TransactionalOperator transaction) {
        this.orders = orders; this.audits = audits; this.transaction = transaction;
    }
    public Mono<Order> find(long id) {
        return orders.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found")));
    }
    public Mono<Order> create(String product, int quantity) {
        return Mono.defer(() -> orders.save(new Order(null, product, quantity, "CREATED", null))
            .flatMap(order -> audits.save(new OrderAudit(null, order.id(), "CREATED")).thenReturn(order)))
            .as(transaction::transactional);
    }
    public Mono<Order> update(long id, String status) {
        List<String> stages = List.of("CREATED", "PACKED", "OUT_FOR_DELIVERY", "DELIVERED");
        return Mono.defer(() -> find(id).flatMap(existing -> {
            int current = stages.indexOf(existing.status());
            if (current == stages.size() - 1 || !stages.get(current + 1).equals(status))
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "use the next delivery stage"));
            return orders.save(new Order(id, existing.product(), existing.quantity(), status, existing.version()))
                .flatMap(saved -> audits.save(new OrderAudit(null, id, status)).thenReturn(saved));
        })).as(transaction::transactional);
    }
    public Flux<Order> events(long id) {
        // Poll current durable state: this is not a replayable history of every transition.
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(1)).take(120).concatMap(tick -> find(id))
            .distinctUntilChanged(Order::status).takeUntil(order -> order.status().equals("DELIVERED"));
    }
}
