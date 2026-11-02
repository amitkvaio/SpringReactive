# 11 · Functional routes and handlers

> Keep URL matching separate from response handling.

## Start with a familiar example

- A receptionist chooses the correct desk while the desk handles the actual work.

## Learn the idea and follow the code

- A RouterFunction matches a request and selects a handler.
- A handler receives ServerRequest and returns Mono<ServerResponse>.
- `ServerResponse.ok().body` streams a publisher into the response body.
- Annotation routes and functional routes use the same reactive HTTP foundation.

- Open [ProductRoutes.java](src/main/java/dev/learning/ProductRoutes.java) and follow the method names in the trace below.

```java
return RouterFunctions.route(GET("/products"), this::products);
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [10-webflux-rest](../10-webflux-rest/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [ProductRoutes.java](src/main/java/dev/learning/ProductRoutes.java) — The route and handler.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) — Route checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. GET /products reaches routes().
2. The GET predicate selects products().
3. WebFlux subscribes to the response publisher and serializes the product sequence.

## Run it locally

- Open a terminal in `11-functional-endpoints`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8081/products after starting the server.

## Expected result

```text
GET /products -> [{"id":1,"name":"tea"},{"id":2,"name":"coffee"}]
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

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Add a route for one drink.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Register GET("/products/1") and return ServerResponse.ok().bodyValue(new Product(1, "tea")).

</details>

## Interview questions and FAQs

**Does a functional handler need @RestController?**

- No, the RouterFunction bean connects requests to handler functions.

**Who subscribes to the response?**

- WebFlux subscribes as part of handling the exchange.

**Can I mix both routing styles?**

- Yes, but keep route ownership clear to avoid confusing overlaps.

## Remember this

- Keep URL matching separate from response handling.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [12 · Call HTTP services with WebClient](../12-webclient/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
