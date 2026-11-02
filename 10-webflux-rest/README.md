# 10 · A small WebFlux REST API

> Accept an order and give useful errors for bad input.

## Start with a familiar example

- A shop counter checks the product and quantity before accepting an order.

## Learn the idea and follow the code

- `@RestController` turns method results into HTTP responses.
- `@GetMapping` and `@PostMapping` select HTTP routes.
- `@Valid` checks the request record before the controller creates an order.
- The concurrent map is a teaching store that resets when the application restarts.
- The framework subscribes to each returned publisher and writes its values to the response.

- Open [OrderController.java](src/main/java/dev/learning/OrderController.java) and follow the method names in the trace below.

```java
return Mono.defer(() -> Mono.justOrEmpty(orders.get(id)))
    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found")));
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [09-stepverifier](../09-stepverifier/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [OrderController.java](src/main/java/dev/learning/OrderController.java) — Routes, validation, and the teaching store.
- [ApiErrors.java](src/main/java/dev/learning/ApiErrors.java) — Consistent validation and lookup errors.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) — HTTP success and failure checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. GET /orders/1 enters OrderController.one().
2. `Mono.defer` reads order 1 when WebFlux subscribes.
3. A found order becomes JSON and a missing order becomes the advice-managed 404 response.

## Run it locally

- Open a terminal in `10-webflux-rest`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8080/orders/1 after starting the server.

## Expected result

```text
GET /orders/1 -> {"id":1,"product":"tea","quantity":2}
GET /orders/9999 -> HTTP 404
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

**Create an order in PowerShell**

```powershell
Invoke-RestMethod http://localhost:8080/orders -Method Post -ContentType application/json -Body '{"product":"coffee","quantity":2}'
```

**Create an order on macOS / Linux**

```bash
curl -i http://localhost:8080/orders -H 'Content-Type: application/json' -d '{"product":"coffee","quantity":2}'
```

- Expect HTTP 201 and a generated ID.

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Reject quantities above ten.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Add @Max(10) to quantity and test a request with quantity 11.

</details>

## Interview questions and FAQs

**Why return Mono instead of calling subscribe()?**

- WebFlux needs the publisher to manage the response, errors, and cancellation.

**Does this controller make a database call?**

- No, it uses an in-memory map so the first HTTP lesson has no infrastructure requirement.

**What validates the request body?**

- Bean Validation applies NotBlank and Positive when @Valid is processed.

**What is the MVC difference?**

- MVC normally uses the servlet model while this application uses the reactive WebFlux stack.

## Remember this

- Accept an order and give useful errors for bad input.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [11 · Functional routes and handlers](../11-functional-endpoints/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
