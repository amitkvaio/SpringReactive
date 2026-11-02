# 17 · PostgreSQL with R2DBC

> Save an order and its audit entry in one reactive transaction.

## Start with a familiar example

- A shop writes an order and its receipt together so neither is left half-finished.

## Learn the idea and follow the code

- R2DBC accesses a relational database through a reactive driver.
- ReactiveCrudRepository returns publishers instead of blocking JDBC results.
- TransactionalOperator scopes the order insert and audit insert to the same reactive transaction.
- An audit failure rolls back the database writes in that transaction.
- HTTP calls and work in a separately subscribed chain are outside this database transaction.
- The page query uses bound parameters, a stable ID order, and a bounded page size.
- The pool permits at most five database connections and waits at most three seconds for acquisition.

- Open [OrderController.java](src/main/java/dev/learning/OrderController.java) and follow the method names in the trace below.

```java
return Mono.defer(() -> orders.save(new Order(null, product, quantity))
    .flatMap(order -> audits.save(new OrderAudit(null, order.id(), "CREATED")).thenReturn(order)))
    .as(transaction::transactional);
```

## Before you start

- Start the documented database and set its password before running this application.

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [16-hot-cold-and-sinks](../16-hot-cold-and-sinks/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [OrderController.java](src/main/java/dev/learning/OrderController.java) — Validated writes and bounded pagination.
- [OrderService.java](src/main/java/dev/learning/OrderService.java) — Order and audit transaction.
- [OrderRepository.java](src/main/java/dev/learning/OrderRepository.java) — Parameterized filtering query.
- [OrderServiceTest.java](src/test/java/dev/learning/OrderServiceTest.java) — Routine service checks.
- [DatabaseIT.java](src/test/java/dev/learning/DatabaseIT.java) — Real PostgreSQL integration checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. POST /orders enters the validated controller.
2. OrderService assembles an order save followed by an audit save.
3. WebFlux subscribes and TransactionalOperator opens the transaction.
4. Successful completion commits both rows and returns the created order.

## Run it locally

- Open a terminal in `17-r2dbc-postgres`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8084/orders after starting the server.

## Expected result

```text
First GET /orders -> []
POST /orders with tea and quantity 2 -> HTTP 201 with a generated ID
GET /orders?term=tea&page=0&size=5 -> matching saved orders
```

## Test it

```powershell
.\mvnw.cmd test
```

```bash
sh mvnw test
```

- Service tests check composition with mocks while DatabaseIT checks real commits and rollback.
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

**Create a saved order**

```powershell
Invoke-RestMethod http://localhost:8084/orders -Method Post -ContentType application/json -Body '{"product":"tea","quantity":2}'
```

```bash
curl -i http://localhost:8084/orders -H 'Content-Type: application/json' -d '{"product":"tea","quantity":2}'
```

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Filter the saved products by a search term.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Create tea and coffee, then call /orders?term=tea&page=0&size=5.

</details>

## Interview questions and FAQs

**Does R2DBC use JPA?**

- No, this lesson uses Spring Data R2DBC repositories and a PostgreSQL R2DBC driver.

**Where does a reactive transaction live?**

- Its resources are propagated through the participating reactive subscription context.

**Can a transaction cover a remote HTTP service?**

- A local database transaction does not atomically commit changes in another service.

**Does a larger connection pool always help?**

- No, excessive connections can overload the database and increase contention.

**Why keep integration tests separate?**

- Routine tests remain runnable without Docker while database behavior is tested explicitly with real infrastructure.

## Remember this

- Save an order and its audit entry in one reactive transaction.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [18 · Reactive MongoDB · Optional](../18-reactive-mongodb/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
