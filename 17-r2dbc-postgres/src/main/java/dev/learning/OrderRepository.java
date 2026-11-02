package dev.learning;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
public interface OrderRepository extends ReactiveCrudRepository<Order,Long> {
    @Query("SELECT * FROM classroom_orders WHERE product ILIKE ('%' || :term || '%') ORDER BY id LIMIT :limit OFFSET :offset")
    Flux<Order> page(@Param("term") String term, @Param("limit") int limit, @Param("offset") long offset);
}
