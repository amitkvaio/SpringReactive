# 21 · Checkpoints and resource cleanup

> Find the failing stage and release resources on every termination path.

## Start with a familiar example

- Return a borrowed book even when you stop reading halfway through.

## Learn the idea and follow the code

- `checkpoint` adds a named assembly clue to an error without recovering from it.
- `using` scopes a synchronously cleaned resource to one subscription.
- `usingWhen` is the alternative when acquisition and cleanup return publishers.
- `doFinally` observes completion, failure, or cancellation but is not asynchronous cleanup composition.
- Global operator debugging captures more information at additional runtime cost.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Flux<String> usingResource(AtomicBoolean closed) {
        return Flux.using(() -> "borrowed-resource", resource -> Flux.just("read", "processed"),
            resource -> closed.set(true));
    }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [20-context-and-event-loops](../20-context-and-event-loops/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. Subscription acquires the teaching resource.
2. The source emits read and processed.
3. `using` invokes cleanup when processing ends.
4. Tests also stop early and fail deliberately to check cleanup.

## Run it locally

- Open a terminal in `21-debugging-and-cleanup`.

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
Demo: [read, processed]
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

- **Exercise:** Rename the checkpoint to include a business stage.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Use checkpoint("payment-validation") and confirm that the suppressed assembly trace contains that label.

</details>

## Interview questions and FAQs

**Does checkpoint catch an error?**

- No, it adds diagnostic information while the error continues downstream.

**When should I use usingWhen?**

- Use it when releasing a resource is asynchronous and must participate in the reactive chain.

**Does cancellation trigger cleanup?**

- using and correctly configured usingWhen clean up when the subscription is cancelled.

**Should global debugging stay enabled everywhere?**

- Evaluate its assembly-capture overhead before using it in a high-throughput deployment.

## Remember this

- Find the failing stage and release resources on every termination path.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [22 · Time, deadlines, and bounded retries](../22-time-and-retries/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
