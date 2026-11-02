package dev.learning;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.mock;
class ProductTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Test void returnsRepositoryValues() {
        ProductRepository repository = mock(ProductRepository.class);
        org.mockito.Mockito.when(repository.findAll()).thenReturn(Flux.just(new Product("1", "tea")));
        StepVerifier.create(new ProductController(repository).all()).expectNext(new Product("1", "tea")).verifyComplete();
    }
    @Test void emptyCollectionCompletes() {
        ProductRepository repository = mock(ProductRepository.class);
        org.mockito.Mockito.when(repository.findAll()).thenReturn(Flux.empty());
        StepVerifier.create(new ProductController(repository).all()).verifyComplete();
    }
}
