package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;

import org.springframework.transaction.reactive.TransactionalOperator;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
class OrderServiceTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void savesOrderAndAudit() {
        OrderRepository orders = mock(OrderRepository.class);
        AuditRepository audits = mock(AuditRepository.class);
        TransactionalOperator tx = mock(TransactionalOperator.class);
        when(tx.transactional(org.mockito.ArgumentMatchers.<Mono<Order>>any())).thenAnswer(call -> call.getArgument(0));
        when(orders.save(any(Order.class))).thenReturn(Mono.just(new Order(7L, "tea", 2, "CREATED", 0L)));
        when(audits.save(any(OrderAudit.class))).thenReturn(Mono.just(new OrderAudit(1L, 7L, "CREATED")));
        StepVerifier.create(new OrderService(orders, audits, tx).create("tea", 2))
            .expectNext(new Order(7L, "tea", 2, "CREATED", 0L)).verifyComplete();
        verify(audits).save(new OrderAudit(null, 7L, "CREATED"));
    }
    @Test void auditFailureRemainsAnError() {
        OrderRepository orders = mock(OrderRepository.class);
        AuditRepository audits = mock(AuditRepository.class);
        TransactionalOperator tx = mock(TransactionalOperator.class);
        when(tx.transactional(org.mockito.ArgumentMatchers.<Mono<Order>>any())).thenAnswer(call -> call.getArgument(0));
        when(orders.save(any(Order.class))).thenReturn(Mono.just(new Order(8L, "tea", 2, "CREATED", 0L)));
        when(audits.save(any(OrderAudit.class))).thenReturn(Mono.error(new IllegalStateException("audit failed")));
        StepVerifier.create(new OrderService(orders, audits, tx).create("tea", 2)).verifyErrorMessage("audit failed");
    }
}
