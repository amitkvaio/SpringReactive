# 07 · Zip, merge, and concat

> Combine related results or join several streams without nested subscriptions.

## Start with a familiar example

- A delivery summary needs both the customer name and the order number.

## Learn the idea and follow the code

- `zip` waits for a value from each source before producing a combined value.
- `merge` subscribes to sources and forwards values as they arrive.
- `concat` completes one source before subscribing to the next.
- An empty Mono cannot provide a value for a zipped pair.
- Merge output order depends on its sources, while concat follows source order.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Mono<String> summary() {
        return Mono.zip(Mono.just("Asha"), Mono.just("order-1"), (customer, order) -> customer + ": " + order);
    }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [06-flatmap-and-ordering](../06-flatmap-and-ordering/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. The customer publisher emits Asha.
2. The order publisher emits order-1.
3. `zip` builds Asha: order-1.
4. `concat` appends ready after the summary completes.

## Run it locally

- Open a terminal in `07-combining-publishers`.

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
Demo: [Asha: order-1, ready]
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

- **Exercise:** Append a delivered stage.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Add Mono.just("delivered") as the final concat source.

</details>

## Interview questions and FAQs

**What if one zipped Mono is empty?**

- The combination completes without producing a pair.

**Does merge preserve source order?**

- No, asynchronously arriving values may interleave.

**When should I use concat?**

- Use it when the next source should start only after the previous source completes.

**Can zip combine Mono<Void> with a value?**

- A Mono<Void> has no item to contribute, so use sequencing operators such as then when appropriate.

## Remember this

- Combine related results or join several streams without nested subscriptions.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [08 · Errors and fallback values](../08-error-handling/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
