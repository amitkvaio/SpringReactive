# 01 · Why reactive programming?

> Describe work as a sequence that runs when someone subscribes.

## Start with a familiar example

- A waiter can take another order while the kitchen prepares food.

## Learn the idea and follow the code

- Synchronous work returns control after the operation finishes.
- Asynchronous work can finish later through a callback or publisher.
- Blocking work holds a thread while it waits.
- Non-blocking I/O lets the thread handle other work while a response is pending.
- Concurrency means multiple tasks can make progress during the same period.
- Parallelism means tasks actually execute at the same time.
- The imperative loop and reactive map here both run synchronously on the caller thread.
- The waiter analogy describes waiting but does not prove the application uses non-blocking I/O.
- MVC is often simpler for blocking JDBC applications, while WebFlux is useful for many concurrent I/O operations.

```java
Flux.range(1, 3).map(id -> "order-" + id);
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Know basic Java methods and collections.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. `imperative()` builds three receipts immediately.
2. `demo()` describes the same transformation with `map`.
3. The console subscribes and collects `order-1` through `order-3`.

## Run it locally

- Open a terminal in `01-reactive-fundamentals`.

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
Demo: [order-1, order-2, order-3]
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

- **Exercise:** Add a fourth order.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change both examples to produce four items and check that the results still match.

</details>

## Interview questions and FAQs

**Is reactive code always asynchronous?**

- No, a Reactor sequence can run synchronously on the subscribing thread.

**Are asynchronous and non-blocking the same?**

- No, asynchronous work can still block a worker thread.

**Is WebFlux always faster than MVC?**

- No, workload, I/O libraries, and resource limits decide the result.

**When is reactive a poor fit?**

- A small blocking CRUD application may gain little while becoming harder to maintain.

## Remember this

- Describe work as a sequence that runs when someone subscribes.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [02 · Reactive Streams and demand](../02-reactive-streams/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
