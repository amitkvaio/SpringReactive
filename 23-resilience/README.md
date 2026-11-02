# 23 · Deadlines and bounded concurrency

> Keep a slow dependency from consuming every available resource.

## Start with a familiar example

- The shop sends only two orders at a time to a busy delivery desk.

## Learn the idea and follow the code

- The flatMap concurrency value bounds active inner subscriptions for this one sequence.
- Each read has a deadline and a visible fallback.
- A concurrency limit inside one request is not a global bulkhead across all HTTP requests.
- A circuit breaker additionally tracks failures and decides whether new calls may start.
- This lesson uses Reactor operators rather than claiming to implement a circuit breaker.
- For a real dependency, classify fallback errors rather than hiding every failure.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Flux<String> work(AtomicInteger active, AtomicInteger peak) {
        return Flux.range(1, 6).flatMap(id -> Mono.defer(() -> {
            peak.accumulateAndGet(active.incrementAndGet(), Math::max);
            return Mono.delay(Duration.ofMillis(20)).map(tick -> "order-" + id)
                .doOnTerminate(active::decrementAndGet);
        }).timeout(Duration.ofSeconds(1)).onErrorReturn("delivery pending"), 2);
    }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [22-time-and-retries](../22-time-and-retries/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. Six IDs wait to be looked up.
2. At most two inner subscriptions are active.
3. Completed reads allow the next IDs to start.
4. The console sorts labels for a stable demonstration.

## Run it locally

- Open a terminal in `23-resilience`.

**Windows PowerShell**

```powershell
.\mvnw.cmd compile exec:java
```

**macOS / Linux**

```bash
sh mvnw compile exec:java
```

## Expected result

```text
Demo: [order-1, order-2, order-3, order-4, order-5, order-6]
```

## Test it

```powershell
.\mvnw.cmd test
```

```bash
sh mvnw test
```

- The tests check the useful behavior rather than relying on console output.
- The console example exits after collecting its finite result.

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Use terminal blocking only to keep this console demo alive until its result arrives.
- Do not copy terminal block() calls into a WebFlux request handler.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Change concurrency from two to one.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Set flatMap concurrency to 1 and verify that peak active work stays at one.

</details>

## Interview questions and FAQs

**Is a timeout a circuit breaker?**

- No, a timeout limits an individual wait while a circuit breaker controls admission using failure state.

**Does flatMap concurrency limit all users?**

- No, the limit applies to that subscription rather than the entire server.

**Can retries worsen an outage?**

- Yes, retries increase traffic and need attempt limits, backoff, and an overall deadline.

**Is a fallback always correct?**

- No, it must represent an honest and useful business response.

## Remember this

- Keep a slow dependency from consuming every available resource.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [24 · Observe work and measure capacity](../24-observability-and-capacity/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
