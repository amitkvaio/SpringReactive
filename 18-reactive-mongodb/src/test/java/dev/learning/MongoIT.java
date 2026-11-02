package dev.learning;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class MongoIT {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Autowired ProductRepository repository;
    @Test void savesAndReadsDocument() {
        StepVerifier.create(repository.save(new Product(null, "integration-tea"))
            .flatMap(saved -> repository.findById(saved.id())))
            .expectNextMatches(product -> product.id() != null && product.name().equals("integration-tea")).verifyComplete();
    }
}
