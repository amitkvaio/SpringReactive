# 03 · Mono and Flux

> Represent one result, several results, or a missing result without using null.

## Start with a familiar example

- A shop gives you one receipt but may show you several drinks.

## Learn the idea and follow the code

- `Mono<T>` emits zero or one item before completing.
- `Flux<T>` emits zero to many items before completing.
- `Mono.empty()` completes without an item.
- `Mono.error()` ends the sequence with an error.
- Null is not a valid emitted item.
- These types do not automatically move work onto another thread.

```java
Mono<String> receipt = Mono.just("receipt-1");
Flux<String> drinks = Flux.just("tea", "coffee", "juice");
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [02-reactive-streams](../02-reactive-streams/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. `receipt(1)` returns a publisher for one receipt.
2. `receipt(9)` returns an empty publisher.
3. `demo()` publishes three drink names when the console subscribes.

## Run it locally

- Open a terminal in `03-mono-and-flux`.

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
Demo: [tea, coffee, juice]
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

- **Exercise:** Show a default receipt for a missing order.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Use receipt(9).defaultIfEmpty("no receipt") and expect one fallback value.

</details>

## Interview questions and FAQs

**Can Mono return no value?**

- Yes, an empty Mono completes without onNext.

**Can Flux emit null?**

- No, use an empty publisher or an explicit value type for absence.

**Can an error be followed by completion?**

- No, an error terminates that subscription.

**Does Mono.just defer its argument?**

- No, Java evaluates the argument immediately before Mono.just is called.

## Remember this

- Represent one result, several results, or a missing result without using null.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [04 · Creation, laziness, and lifecycle](../04-publishers-and-lifecycle/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
