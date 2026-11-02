# 24 · Observe work and measure capacity

> Measure requests and reject excess work before resources run out.

## Start with a familiar example

- A shop has four service desks and counts completed orders instead of accepting an endless queue.

## Learn the idea and follow the code

- Actuator exposes health and metrics for the local demonstration.
- Micrometer records a counter and a gauge.
- A non-blocking semaphore check admits at most four active work subscriptions across requests.
- `doFinally` returns capacity after completion, failure, or cancellation.
- The 100 ms delay represents waiting rather than CPU work.
- A sequential latency sample is not a throughput benchmark or a proof that WebFlux is faster.

- Open [CapacityController.java](src/main/java/dev/learning/CapacityController.java) and follow the method names in the trace below.

```java
return Mono.delay(Duration.ofMillis(100)).map(t -> Map.of("status", "done"))
    .doOnSuccess(value -> completed.increment()).doFinally(signal -> permits.release());
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [23-resilience](../23-resilience/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [CapacityController.java](src/main/java/dev/learning/CapacityController.java) — Metrics and admission control.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) — Capacity and HTTP checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. GET /work tries to acquire one permit.
2. If all four permits are busy, the server returns 429.
3. A timer completes admitted work and increments the counter.
4. The permit is released when the subscription terminates.

## Run it locally

- Open a terminal in `24-observability-and-capacity`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8087/work after starting the server.

## Expected result

```text
GET /work -> {"status":"done"}
GET /actuator/health -> {"status":"UP"}
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

**Inspect metrics**

- Open http://localhost:8087/actuator/metrics/classroom.work.completed after sending work.
- Run `./measure.ps1` in PowerShell for a small sequential latency sample.
- Use a load generator with controlled concurrency for a real capacity experiment.

**Package and run with Docker**

```powershell
.\mvnw.cmd package
docker compose up --build -d
docker compose down
```

```bash
sh mvnw package
docker compose up --build -d
docker compose down
```

- The container runs as a non-root user with a local Compose memory limit.
- Restrict management endpoints and add TLS before deploying outside your machine.
- Keep database pool size, timeouts, and application concurrency aligned with measured capacity.

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Reduce capacity to two and compare rejection behavior.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change the semaphore permits to 2 and use concurrent clients to observe 429 responses.

</details>

- A trace links spans across service calls while a request ID only provides correlation unless tracing instrumentation creates those spans.

## Interview questions and FAQs

**Why limit concurrency if I use non-blocking I/O?**

- Connections, memory, remote services, and database pools still have finite capacity.

**What does a counter show?**

- It accumulates events such as successfully completed work.

**Should request IDs be metric tags?**

- No, unbounded tag values can create excessive metric cardinality.

**Does this include distributed tracing?**

- It includes local HTTP metrics while external tracing requires a compatible exporter and collector.

**What should a load test report?**

- Report workload, concurrency, throughput, latency percentiles, errors, and resource usage.

## Remember this

- Measure requests and reject excess work before resources run out.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [25 · WebSocket messages · Optional](../25-websockets/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
