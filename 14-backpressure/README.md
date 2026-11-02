# 14 · Demand, buffering, and dropping

> Make a deliberate choice when producers are faster than consumers.

## Start with a familiar example

- A helper delivers boxes in batches because the receiver can carry only two.

## Learn the idea and follow the code

- `limitRate(2)` reshapes upstream requests into small batches.
- The buffer test sends three items while downstream demand is zero to demonstrate overflow.
- `onBackpressureBuffer(2)` holds at most two pending items before failing.
- `onBackpressureDrop` discards items when demand is absent.
- A drop policy loses data and is unsuitable for orders that must be saved.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Flux<Integer> demo() { return Flux.range(1, 6).limitRate(2); }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [13-server-sent-events](../13-server-sent-events/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. The range source offers six numbers.
2. `limitRate` requests small batches upstream.
3. The console eventually collects all six values.
4. Tests separately show overflow and dropping with a source that ignores demand.

## Run it locally

- Open a terminal in `14-backpressure`.

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
Demo: [1, 2, 3, 4, 5, 6]
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

- **Exercise:** Increase the bounded buffer to five.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Use onBackpressureBuffer(5) and request all five items to complete without overflow.

</details>

## Interview questions and FAQs

**Does limitRate slow the producer with a timer?**

- No, it changes request sizes rather than introducing a delay.

**Does backpressure guarantee unlimited scalability?**

- No, CPU, connection pools, queues, and remote systems still have limits.

**When is dropping acceptable?**

- It may suit replaceable live measurements but usually not financial or order events.

**Is an unbounded buffer safe?**

- No, a sustained rate mismatch can exhaust memory.

## Remember this

- Make a deliberate choice when producers are faster than consumers.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [15 · Schedulers and blocking adapters](../15-schedulers-and-blocking/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
