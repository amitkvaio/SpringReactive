# 02 · Reactive Streams and demand

> Let a receiver request only the items it can handle.

## Start with a familiar example

- A student asks for two worksheets instead of carrying the entire stack.

## Learn the idea and follow the code

- A Publisher offers a sequence.
- A Subscriber receives values and terminal signals.
- A Subscription lets the receiver request items or cancel.
- A Processor is both a Subscriber and a Publisher.
- `BaseSubscriber` lets this console lesson request exactly two items.
- The console main requests all five items when collecting its result.
- Demand controls item delivery but does not automatically bound every buffer.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Flux<Integer> demo() { return Flux.range(1, 5); }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [01-reactive-fundamentals](../01-reactive-fundamentals/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. `Flux.range` offers numbers 1 through 5.
2. `requestTwo()` subscribes and calls `request(2)`.
3. Only 1 and 2 reach that subscriber until it requests more.
4. The main demo uses a separate subscription and prints all five values.

## Run it locally

- Open a terminal in `02-reactive-streams`.

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
Demo: [1, 2, 3, 4, 5]
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

- **Exercise:** Request three worksheets instead of two.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change request(2) to request(3) and update the assertion to [1, 2, 3].

</details>

## Interview questions and FAQs

**Can a publisher send more values than requested?**

- A compliant publisher must not send more onNext signals than the outstanding demand.

**Is completion an item?**

- No, completion and errors are terminal signals rather than values.

**What happens after cancellation?**

- The source should stop producing for that subscription as soon as practical.

**Should a controller call subscribe()?**

- No, return the publisher and let WebFlux manage the subscription.

## Remember this

- Let a receiver request only the items it can handle.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [03 · Mono and Flux](../03-mono-and-flux/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
