# 04 · Creation, laziness, and lifecycle

> Choose a publisher factory that runs work at the right time.

## Start with a familiar example

- Writing a recipe prepares instructions, while cooking starts when someone requests a meal.

## Learn the idea and follow the code

- `just` captures an already evaluated value.
- `fromSupplier` computes a value for each subscription.
- `defer` creates a publisher for each subscription.
- `fromIterable` reads a collection as a sequence.
- `justOrEmpty` converts a nullable value into zero or one item.
- `doOnCancel` observes cancellation without creating a new subscription.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Mono<Integer> fresh(AtomicInteger counter) {
        return Mono.defer(() -> Mono.just(counter.incrementAndGet()));
    }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [03-mono-and-flux](../03-mono-and-flux/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. `fresh(counter)` assembles a recipe without increasing the counter.
2. The first subscription receives 1 and the second receives 2.
3. The finite main demo prints names of the successful lifecycle stages.

## Run it locally

- Open a terminal in `04-publishers-and-lifecycle`.

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
Demo: [subscribe, next, complete]
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

- **Exercise:** Count three subscriptions.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Subscribe to the same fresh publisher three times and expect 1, 2, and 3.

</details>

## Interview questions and FAQs

**Why does work run twice?**

- A cold source commonly repeats its work for each subscription.

**What is the difference between defer and just?**

- defer invokes its factory at subscription while just stores a value already evaluated by Java.

**When should I use fromCallable?**

- Use it for a synchronous call that may throw and add boundedElastic when that call blocks.

**Does cancellation undo completed work?**

- No, cancellation stops future work where supported but cannot undo a completed side effect.

## Remember this

- Choose a publisher factory that runs work at the right time.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [05 · Map and filter](../05-map-and-filter/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
