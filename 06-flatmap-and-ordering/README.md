# 06 · FlatMap and ordering

> Combine asynchronous lookups while choosing the output order you need.

## Start with a familiar example

- Three kitchen orders take different times, so the last order can finish first.

## Learn the idea and follow the code

- `lookup` receives an ID and returns a delayed Mono containing its order label.
- `flatMap` subscribes to inner publishers concurrently up to the given limit.
- `concatMap` subscribes to one inner publisher at a time in input order.
- `flatMapSequential` allows concurrent subscriptions but buffers results to retain input order.
- `Mono.delay` uses a timer scheduler, while flatMap itself does not create threads.
- An inner failure normally terminates the combined sequence and cancels remaining work.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Mono<String> lookup(int id) {
        return Mono.delay(Duration.ofMillis((4 - id) * 20L)).map(tick -> "order-" + id);
    }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [05-map-and-filter](../05-map-and-filter/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. IDs 1, 2, and 3 each create a lookup Mono.
2. The third lookup has the shortest delay.
3. `demo()` uses flatMapSequential so labels still leave in order 1, 2, 3.

## Run it locally

- Open a terminal in `06-flatmap-and-ordering`.

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

- **Exercise:** Compare demo() with unordered().
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Return unordered() from demo() and expect order-3, order-2, order-1 in this timed example.

</details>

## Interview questions and FAQs

**Why use flatMap for a Mono-returning lookup?**

- It composes the inner publisher into the outer sequence instead of returning nested Monos.

**Does flatMap preserve ordering?**

- It does not guarantee the input order of values from concurrent inner publishers.

**How does flatMapSequential differ from concatMap?**

- It subscribes to multiple inner publishers while buffering to preserve output order.

**Is concurrency the same as parallelism?**

- No, concurrent inner subscriptions do not prove simultaneous execution on multiple CPU cores.

## Remember this

- Combine asynchronous lookups while choosing the output order you need.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [07 · Zip, merge, and concat](../07-combining-publishers/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
