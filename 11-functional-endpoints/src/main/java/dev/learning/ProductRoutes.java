package dev.learning;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
@Configuration
public class ProductRoutes {
    record Product(int id, String name) {}
    Mono<ServerResponse> products(ServerRequest request) {
        return ServerResponse.ok().body(Flux.just(new Product(1, "tea"), new Product(2, "coffee")), Product.class);
    }
    @Bean RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route(GET("/products"), this::products);
    }
}
