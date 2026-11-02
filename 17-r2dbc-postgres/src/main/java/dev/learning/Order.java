package dev.learning;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
@Table("classroom_orders")
public record Order(@Id Long id, String product, int quantity) {}
