# 09 · Test with StepVerifier

> Check the signals a subscriber really receives.

## Start with a familiar example

- Check each item in a parcel and confirm that no extra item arrives.

## Learn the idea and follow the code

- `StepVerifier.create` prepares a subscriber for a publisher.
- `expectNext` checks emitted values in order.
- `verifyComplete` subscribes and requires successful completion.
- `verifyError` checks terminal failure.
- `thenRequest` checks demand and `thenCancel` checks cancellation.
- Virtual time advances Reactor timers without making the test wait for real hours.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Flux<Integer> demo() { return Flux.range(1, 4).filter(n -> n % 2 == 0); }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [08-error-handling](../08-error-handling/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. Numbers 1 through 4 enter filter.
2. Only 2 and 4 pass.
3. The test expects both values followed by completion.

## Run it locally

- Open a terminal in `09-stepverifier`.

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
Demo: [2, 4]
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

- **Exercise:** Test a sequence containing only odd numbers.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change the predicate to n % 2 != 0 and expect 1 and 3.

</details>

## Interview questions and FAQs

**Why can a StepVerifier test do nothing?**

- Without a verify method, the scenario is only assembled.

**Why create the timed publisher inside the supplier?**

- The virtual scheduler must be installed before timing operators are created.

**Should tests use Thread.sleep?**

- No, use virtual time or explicit signals rather than arbitrary delays.

**Does StepVerifier test HTTP status codes?**

- Use WebTestClient for HTTP behavior and StepVerifier for publisher signals.

## Remember this

- Check the signals a subscriber really receives.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [10 · A small WebFlux REST API](../10-webflux-rest/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
