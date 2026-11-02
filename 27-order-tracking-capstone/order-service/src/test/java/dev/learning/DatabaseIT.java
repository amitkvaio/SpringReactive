package dev.learning;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT, properties="DEMO_PASSWORD=integration-only")
class DatabaseIT {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Autowired OrderService service;
    @Autowired OrderRepository orders;
    @Autowired DatabaseClient database;
    @Autowired TransactionalOperator transaction;
    @Test void savesOrderWithAudit() {
        StepVerifier.create(service.create("integration-tea", 2).flatMap(order ->
            database.sql("SELECT COUNT(*) AS total FROM order_audit WHERE order_id = :id").bind("id", order.id())
                .map((row, metadata) -> row.get("total", Long.class)).one()))
            .expectNext(1L).verifyComplete();
    }
    @Test void rollsBackOnFailure() {
        String product = "rollback-" + java.util.UUID.randomUUID();
        Mono<Order> failing = orders.save(new Order(null, product, 1, "CREATED", null))
            .flatMap(saved -> Mono.<Order>error(new IllegalStateException("rollback"))).as(transaction::transactional);
        StepVerifier.create(failing).verifyErrorMessage("rollback");
        StepVerifier.create(orders.page(product, 5, 0)).verifyComplete();
    }
    @Test void rejectsAStaleVersion() {
        Mono<Order> staleWrite = service.create("optimistic-tea", 1).flatMap(order -> {
            Order change = new Order(order.id(), order.product(), order.quantity(), "PACKED", order.version());
            return orders.save(change).then(orders.save(change));
        });
        StepVerifier.create(staleWrite).verifyError(org.springframework.dao.OptimisticLockingFailureException.class);
    }
}
