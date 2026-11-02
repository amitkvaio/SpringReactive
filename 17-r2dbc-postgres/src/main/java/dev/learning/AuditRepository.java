package dev.learning;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface AuditRepository extends ReactiveCrudRepository<OrderAudit,Long> {}
