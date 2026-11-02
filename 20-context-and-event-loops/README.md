# 20 · Reactor Context and event loops

> Carry a request ID even when reactive work changes threads.

## Start with a familiar example

- An order ticket follows the order between desks instead of belonging to one employee.

## Learn the idea and follow the code

- An event loop serves many connections and must not be held by blocking work.
- Reactor Context belongs to a subscription rather than a thread.
- `contextWrite` enriches the context visible upstream.
- `deferContextual` reads context when its publisher is subscribed.
- ThreadLocal values do not automatically follow scheduler changes.
- The response prints the actual current thread instead of assuming an entire request stays on one thread.

- Open [CorrelationFilter.java](src/main/java/dev/learning/CorrelationFilter.java) and follow the method names in the trace below.

```java
return chain.filter(exchange).contextWrite(context -> context.put(KEY, id));
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [19-reactive-security](../19-reactive-security/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [CorrelationFilter.java](src/main/java/dev/learning/CorrelationFilter.java) — Safe request correlation.
- [ContextController.java](src/main/java/dev/learning/ContextController.java) — Reads context across a scheduler boundary.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) — Correlation checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. CorrelationFilter accepts a safe request ID or generates one.
2. The filter attaches it to Reactor Context and the response header.
3. ContextController switches downstream execution to a scheduler.
4. deferContextual reads the same request ID and returns it with the observed thread.

## Run it locally

- Open a terminal in `20-context-and-event-loops`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8086/context after starting the server.

## Expected result

```text
GET /context with X-Request-Id: class-42 -> {"requestId":"class-42","thread":"parallel-<n>"}
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

- **Exercise:** Send your own classroom request ID.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Run curl with -H "X-Request-Id: lesson-20" and check the header and JSON value.

</details>

## Interview questions and FAQs

**Is Reactor Context a global map?**

- No, it is associated with a subscriber and propagated through the reactive chain.

**Why can ThreadLocal fail here?**

- The work can resume on a different thread whose thread-local state is different.

**Where should contextWrite go?**

- Place it so that the upstream context-reading stage sees its values.

**Does WebFlux use one thread per request?**

- No, event-loop threads can serve many requests and scheduler boundaries can move work.

## Remember this

- Carry a request ID even when reactive work changes threads.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [21 · Checkpoints and resource cleanup](../21-debugging-and-cleanup/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
