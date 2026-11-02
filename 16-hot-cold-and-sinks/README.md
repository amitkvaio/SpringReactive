# 16 · Cold sources, sharing, and sinks

> Choose whether subscribers repeat work or share a live sequence.

## Start with a familiar example

- A fresh recording starts for each viewer, while a live announcement is shared.

## Learn the idea and follow the code

- Cold publishers commonly start their work separately for each subscriber.
- `share` shares an active upstream subscription and does not automatically replay history.
- `cache` reuses a Mono result, including an error unless a different cache policy is configured.
- A replay sink stores a bounded number of recent signals for late subscribers.
- Inspect every sink emission result because concurrent emission or termination can reject a signal.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Mono<Integer> cached(AtomicInteger calls) {
        return Mono.fromSupplier(calls::incrementAndGet).cache();
    }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [15-schedulers-and-blocking](../15-schedulers-and-blocking/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. The demo creates a replay sink for this subscription.
2. It emits packed and shipped before completing.
3. A late subscriber receives the two retained values and completion.

## Run it locally

- Open a terminal in `16-hot-cold-and-sinks`.

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
Demo: [packed, shipped]
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

- **Exercise:** Retain only the latest announcement.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Use replay().limit(1) and expect only shipped from the late subscriber.

</details>

## Interview questions and FAQs

**Is cache always a performance improvement?**

- No, cached results may become stale and retained data consumes memory.

**Can a sink replace a durable broker?**

- No, its history is local to one process and disappears on restart.

**What if tryEmitNext fails?**

- Inspect the EmitResult and apply an explicit retry, rejection, or loss policy.

**Does share replay old values?**

- No, use replay or a suitable cache when history is required.

## Remember this

- Choose whether subscribers repeat work or share a live sequence.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [17 · PostgreSQL with R2DBC](../17-r2dbc-postgres/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
