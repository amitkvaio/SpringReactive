# Capstone · Order service

> Place an order, save it safely, and follow its delivery status.

## Start with a familiar example

- A shop accepts an order, records its receipt, and lets a customer watch delivery progress.

## Learn the idea and follow the code

- OrderService saves each order change and its audit row inside one reactive database transaction.
- WebClient calls the independent local delivery service for an ETA.
- Only the read-only ETA call has automatic bounded retries.
- The SSE endpoint polls durable current state once per second and emits changed statuses.
- Optimistic locking rejects conflicting updates instead of silently overwriting them.
- Reactive security protects the API and Reactor Context carries X-Request-Id.
- Actuator reports health and request metrics while a custom counter counts new orders.

- Open [OrderController.java](src/main/java/dev/learning/OrderController.java) and follow the method names in the trace below.

```java
return Mono.defer(() -> orders.save(new Order(null, product, quantity, "CREATED", null))
    .flatMap(order -> audits.save(new OrderAudit(null, order.id(), "CREATED")).thenReturn(order)))
    .as(transaction::transactional);
```

## Before you start

- Set `DEMO_PASSWORD` using the credential commands below before starting the server.

- Start the documented database and set its password before running this application.

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Read [the capstone walkthrough](../README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [OrderController.java](src/main/java/dev/learning/OrderController.java) — HTTP requests and SSE response.
- [OrderService.java](src/main/java/dev/learning/OrderService.java) — Transactions, transitions, and status snapshots.
- [DeliveryClient.java](src/main/java/dev/learning/DeliveryClient.java) — Correlated HTTP reads with bounded retries.
- [SecurityConfig.java](src/main/java/dev/learning/SecurityConfig.java) — Environment-based demo authentication.
- [OrderControllerTest.java](src/test/java/dev/learning/OrderControllerTest.java) — Missing IDs, transitions, and stream checks.
- [DatabaseIT.java](src/test/java/dev/learning/DatabaseIT.java) — Real persistence and rollback checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- The WebClient starter supplies the configured client builder for delivery calls.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. POST /orders enters reactive security and request correlation filters.
2. The validated controller delegates to OrderService.
3. WebFlux subscribes and the order plus audit transaction commits.
4. GET /orders/{id}/delivery calls the local stub through WebClient.
5. GET /orders/{id}/events streams changed database statuses until DELIVERED or the two-minute polling window ends.

## Run it locally

- Open a terminal in `27-order-tracking-capstone/order-service`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8091/orders after starting the server.

## Expected result

```text
POST /orders -> HTTP 201, status CREATED, generated id
GET /orders/{id}/delivery -> etaMinutes 15
PATCH next statuses -> PACKED, OUT_FOR_DELIVERY, DELIVERED
SSE -> observed changed statuses
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

**Start PostgreSQL before the application**

```powershell
$env:DB_PASSWORD = "classroom-local-only"
docker compose up -d --wait
.\mvnw.cmd spring-boot:run
```

```bash
export DB_PASSWORD=classroom-local-only
docker compose up -d --wait
sh mvnw spring-boot:run
```

- `DB_HOST`, `DB_PORT`, `DB_NAME`, and `DB_USER` override the local database settings.
- Use the same environment variables for Compose and the application.
- Startup creates the lesson tables if they do not exist.
- Stop the application with Ctrl+C and stop PostgreSQL with `docker compose down`.
- `docker compose down -v` also deletes this lesson's stored database data.

**Run the separate database integration checks while PostgreSQL is running**

```powershell
.\mvnw.cmd -Pintegration verify
```

```bash
sh mvnw -Pintegration verify
```

- Routine `test` runs mock-based service checks without Docker.
- The integration profile verifies real persistence and transaction rollback.
- Integration checks need a running database and are not silently skipped.

**Set local teaching credentials before the run command**

```powershell
$env:DEMO_USER = "student"
$env:DEMO_PASSWORD = "local-demo-only"
```

```bash
export DEMO_USER=student
export DEMO_PASSWORD=local-demo-only
```

- These are disposable classroom credentials rather than production secrets.
- An unauthenticated request returns HTTP 401.
- HTTP Basic over plain HTTP is suitable only for this local demo.
- CSRF protection is disabled for this stateless command-line API and must be reconsidered before adding browser credentials or cookies.

**Send an authenticated request**

```powershell
curl.exe -u student:local-demo-only http://localhost:8091/orders
```

```bash
curl -u student:local-demo-only http://localhost:8091/orders
```

- Start the sibling delivery service on port 8092 before requesting an ETA.
- See [the capstone classroom walkthrough](../README.md) for complete request commands.
- Missing IDs return 404 before SSE headers are committed.
- Once SSE starts, a later database failure terminates the stream rather than changing an already sent HTTP status.

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Follow one order from creation to delivery.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Use the parent capstone guide to create an order, open SSE, and patch each next stage with a pause between changes.

</details>

## Interview questions and FAQs

**Can retrying POST create duplicate orders?**

- Yes, this teaching API has no idempotency-key store and deliberately does not automatically retry writes.

**Is SSE a durable event log here?**

- No, it reports current-state snapshots and can miss transitions that happen between polls.

**What happens after a restart?**

- Orders remain in PostgreSQL while clients must reconnect and read the latest status.

**Would an in-memory sink work across instances?**

- No, sink history and subscribers belong to one process and disappear on restart.

**What happens when two users update one order?**

- The version column enables optimistic locking and a stale update returns 409.

**Does an ETA failure roll back the order?**

- No, the remote read is outside the database transaction and returns a visible pending fallback.

## Remember this

- Place an order, save it safely, and follow its delivery status.
- [Back to the learning roadmap](../../ROADMAP.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
