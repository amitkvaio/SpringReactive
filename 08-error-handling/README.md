# 08 · Errors and fallback values

> Choose a useful response when a reactive operation fails.

## Start with a familiar example

- If the delivery desk is unavailable, offer collection at the shop.

## Learn the idea and follow the code

- An error is terminal for the failed upstream subscription.
- `onErrorResume` replaces it with a fallback publisher.
- `onErrorReturn` supplies a fixed fallback value.
- `onErrorMap` changes the error type while preserving failure.
- `doOnError` observes an error but does not recover from it.
- Recover only from failures for which the fallback is meaningful.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Mono<String> remote() { return Mono.error(new IllegalStateException("delivery unavailable")); }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [07-combining-publishers](../07-combining-publishers/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. `remote()` signals delivery unavailable on subscription.
2. `onErrorResume` selects the collection fallback.
3. The console receives collect in store and normal completion.

## Run it locally

- Open a terminal in `08-error-handling`.

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
Demo: [collect in store]
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

- **Exercise:** Return a different fallback only for a timeout.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Use onErrorResume with TimeoutException.class and leave other failures unchanged.

</details>

## Interview questions and FAQs

**Does onErrorResume restart the failed sequence?**

- No, it switches to another publisher after the original sequence fails.

**Does doOnError handle an error?**

- No, the error continues downstream unless another operator recovers.

**When should I use onErrorMap?**

- Use it when callers need a domain error instead of a lower-level exception.

**Should every exception return success?**

- No, unexpected failures should remain visible rather than being hidden by a success-shaped fallback.

## Remember this

- Choose a useful response when a reactive operation fails.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [09 · Test with StepVerifier](../09-stepverifier/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
