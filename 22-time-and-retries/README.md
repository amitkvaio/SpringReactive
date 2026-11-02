# 22 · Time, deadlines, and bounded retries

> Stop stalled work and retry only temporary failures within a fixed budget.

## Start with a familiar example

- Ask the delivery desk again after a short wait, then stop after two retries.

## Learn the idea and follow the code

- `timeout` fails when a source does not produce within the configured interval.
- `Retry.backoff(2, ...)` permits two retries after the first attempt.
- Backoff increases the gap between retries and jitter can spread client load.
- The teaching test disables jitter to make virtual-time expectations predictable.
- Retry means resubscription and can repeat database writes or remote side effects.
- Only retry writes when an idempotency strategy makes repetition safe.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Mono<String> retryRead(AtomicInteger attempts) {
        return Mono.defer(() -> attempts.incrementAndGet() < 3
            ? Mono.<String>error(new IllegalStateException("temporary")) : Mono.just("delivery ready"))
            .retryWhen(Retry.backoff(2, Duration.ofMillis(100)).jitter(0)
                .filter(IllegalStateException.class::isInstance));
    }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [21-debugging-and-cleanup](../21-debugging-and-cleanup/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. The first two deferred delivery reads fail.
2. The retry policy waits 100 ms and then 200 ms.
3. The third attempt returns delivery ready.
4. A separate test verifies that a stalled source times out.

## Run it locally

- Open a terminal in `22-time-and-retries`.

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
Demo: [delivery ready]
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

- **Exercise:** Allow only one retry.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change backoff(2, ...) to backoff(1, ...) and assert retry exhaustion instead of success.

</details>

## Interview questions and FAQs

**Does retry(2) mean two total calls?**

- No, it permits the original attempt plus two retries.

**Why avoid retrying every error?**

- Validation and authorization failures are unlikely to improve with another identical call.

**Where should an overall timeout go?**

- Place it after the retry chain when it must cover all attempts and backoff waits.

**What is idempotency?**

- Repeating the same operation has the same intended effect as performing it once.

## Remember this

- Stop stalled work and retry only temporary failures within a fixed budget.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [23 · Deadlines and bounded concurrency](../23-resilience/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
