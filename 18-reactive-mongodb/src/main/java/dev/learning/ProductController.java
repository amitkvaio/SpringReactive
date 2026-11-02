package dev.learning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {
    private final ProductRepository products;
    public ProductController(ProductRepository products) { this.products = products; }
    @GetMapping("/products") Flux<Product> all() { return products.findAll().take(20); }
    @PostMapping("/products/demo") @ResponseStatus(HttpStatus.CREATED)
    Mono<Product> create() { return products.save(new Product(null, "tea")); }
}
