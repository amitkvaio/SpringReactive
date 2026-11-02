# 18 · Reactive MongoDB · Optional

> Read and save documents through a reactive repository.

## Start with a familiar example

- A shop stores flexible product cards rather than rows with a fixed relational schema.

## Learn the idea and follow the code

- ReactiveMongoRepository uses the reactive MongoDB driver.
- `@Document` names the collection and `@Id` marks the document key.
- The first GET returns an empty list because this lesson does not seed data.
- The teaching endpoint limits a response to twenty documents but does not provide production pagination.
- This alternative does not replace the required PostgreSQL lessons.

- Open [ProductController.java](src/main/java/dev/learning/ProductController.java) and follow the method names in the trace below.

```java
return products.save(new Product(null, "tea"));
```

## Before you start

- Start the documented database and set its password before running this application.

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [17-r2dbc-postgres](../17-r2dbc-postgres/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [ProductController.java](src/main/java/dev/learning/ProductController.java) — Small document API.
- [ProductRepository.java](src/main/java/dev/learning/ProductRepository.java) — Reactive MongoDB access.
- [ProductTest.java](src/test/java/dev/learning/ProductTest.java) — Routine publisher checks.
- [MongoIT.java](src/test/java/dev/learning/MongoIT.java) — Real MongoDB persistence check.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. POST /products/demo enters ProductController.
2. The repository inserts a tea document when WebFlux subscribes.
3. GET /products reads up to twenty stored documents.

## Run it locally

- Open a terminal in `18-reactive-mongodb`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8090/products after starting the server.

## Expected result

```text
First GET /products -> []
POST /products/demo -> HTTP 201 with an ID and name tea
```

## Test it

```powershell
.\mvnw.cmd test
```

```bash
sh mvnw test
```

- The tests check the useful behavior rather than relying on console output.
- Press Ctrl+C to stop the server.

**Start the local database before running the application**

```powershell
$env:MONGO_PASSWORD = "mongo-local-only"
docker compose up -d --wait
.\mvnw.cmd spring-boot:run
```

```bash
export MONGO_PASSWORD=mongo-local-only
docker compose up -d --wait
sh mvnw spring-boot:run
```

```powershell
Invoke-RestMethod http://localhost:8090/products/demo -Method Post
.\mvnw.cmd -Pintegration verify
```

```bash
curl -X POST http://localhost:8090/products/demo
sh mvnw -Pintegration verify
```

- MongoIT requires the database and is separate from routine mock-based tests.
- Stop MongoDB with `docker compose down` after stopping the application.
- `docker compose down -v` removes this lesson's stored documents.
- Use a simple local alphanumeric password or URL-encode special characters in the connection URI.

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Add a derived query for product names.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Declare Flux<Product> findByName(String name) on the repository and call it from a route.

</details>

## Interview questions and FAQs

**Is MongoRepository reactive?**

- Use ReactiveMongoRepository because the ordinary MongoRepository uses blocking access.

**Does this standalone MongoDB support multi-document transactions?**

- Multi-document transactions need a replica set or supported sharded deployment.

**Can I skip this lesson?**

- Yes, the required relational path continues with PostgreSQL and R2DBC.

## Remember this

- Read and save documents through a reactive repository.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [19 · Reactive Spring Security](../19-reactive-security/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
