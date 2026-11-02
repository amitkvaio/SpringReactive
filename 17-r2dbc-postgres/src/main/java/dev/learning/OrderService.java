package dev.learning;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
@Service
public class OrderService {
    private final OrderRepository orders;
    private final AuditRepository audits;
    private final TransactionalOperator transaction;
    public OrderService(OrderRepository orders, AuditRepository audits, TransactionalOperator transaction) {
        this.orders = orders; this.audits = audits; this.transaction = transaction;
    }
    public Mono<Order> create(String product, int quantity) {
        return Mono.defer(() -> orders.save(new Order(null, product, quantity))
            .flatMap(order -> audits.save(new OrderAudit(null, order.id(), "CREATED")).thenReturn(order)))
            .as(transaction::transactional);
    }
}
