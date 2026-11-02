package dev.learning;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
@Table("order_audit")
public record OrderAudit(@Id Long id, Long orderId, String action) {}
