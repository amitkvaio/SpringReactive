# 05 · Map and filter

> Keep useful values and turn each one into the form your screen needs.

## Start with a familiar example

- A shop shows price labels only for drinks costing 25 or less.

## Learn the idea and follow the code

- `filter` receives a price and keeps it only when the predicate is true.
- `map` receives one price and returns one label.
- The functions run when upstream values arrive after subscription.
- These synchronous operators preserve the relative order of retained values.
- A thrown exception becomes an error and successful upstream completion flows downstream.

```java
Flux.just(10, 25, 40)
    .filter(price -> price <= 25)
    .map(price -> "Price: " + price);
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [04-publishers-and-lifecycle](../04-publishers-and-lifecycle/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. Prices 10, 25, and 40 enter the chain.
2. `filter` removes 40.
3. `map` converts 10 and 25 into labels.
4. The console receives two labels and completion.

## Run it locally

- Open a terminal in `05-map-and-filter`.

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
Demo: [Price: 10, Price: 25]
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

- **Exercise:** Include products up to 40.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change the filter to price <= 40 and expect a third label.

</details>

## Interview questions and FAQs

**Does filter change a value?**

- No, it decides whether a value continues downstream.

**Can map return Mono?**

- It can, but the result becomes a nested publisher unless you compose it with flatMap.

**Does map start a thread?**

- No, map runs in the current signal-processing thread.

**What happens when the predicate throws?**

- The sequence terminates with that error.

## Remember this

- Keep useful values and turn each one into the form your screen needs.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [06 · FlatMap and ordering](../06-flatmap-and-ordering/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
