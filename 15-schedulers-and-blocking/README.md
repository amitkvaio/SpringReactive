# 15 · Schedulers and blocking adapters

> Keep unavoidable blocking work away from event-loop threads.

## Start with a familiar example

- A shop assigns slow paperwork to a separate worker so the counter stays available.

## Learn the idea and follow the code

- `subscribeOn` chooses where source subscription and source work begin.
- `publishOn` moves downstream signal processing to its scheduler.
- `fromCallable` delays a synchronous call until subscription and converts thrown exceptions to errors.
- `boundedElastic` limits worker growth and queued work but does not make the legacy operation non-blocking.
- The callable reads [a local receipt file](src/main/resources/legacy-receipt.txt) with blocking file I/O on that bounded worker.
- The demo prints actual scheduler thread names without inventing a fixed thread sequence.
- Never use a parallel scheduler for blocking JDBC or file operations.

- Open [Lesson.java](src/main/java/dev/learning/Lesson.java) and follow the method names in the trace below.

```java
static Mono<String> legacyThread() {
        return Mono.fromCallable(() -> {
            // Classpath resource reads are blocking even when the file is small.
            try (var input = Lesson.class.getResourceAsStream("/legacy-receipt.txt")) {
                if (input == null) throw new IllegalStateException("legacy receipt is missing");
                String receipt = new String(input.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8).trim();
                return receipt + " on " + Thread.currentThread().getName();
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [14-backpressure](../14-backpressure/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [Lesson.java](src/main/java/dev/learning/Lesson.java) — The small runnable example.
- [LessonTest.java](src/test/java/dev/learning/LessonTest.java) — Checks the sequence behavior.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- Reactor Core supplies publishers and operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. The console subscribes to legacyThread().
2. The callable runs on a boundedElastic worker.
3. The second source moves its map onto a parallel worker.
4. Thread suffixes depend on the current process.

## Run it locally

- Open a terminal in `15-schedulers-and-blocking`.

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
Demo: [legacy on boundedElastic-<n>, label on parallel-<n>]
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

- **Exercise:** Print a thread name before publishOn as well.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Add doOnNext before publishOn and compare the observed names without asserting a universal thread name.

</details>

## Interview questions and FAQs

**Does subscribeOn subscribe immediately?**

- No, it chooses the scheduler used when subscription happens.

**Does publishOn move earlier operators?**

- No, it affects downstream processing from its position in the chain.

**Is wrapping JDBC in Mono enough?**

- No, defer the JDBC call and isolate it on a bounded scheduler while accepting that it still blocks.

**Can BlockHound be enabled on every JDK?**

- Instrumentation support is version-dependent and must be checked before adding it.

## Remember this

- Keep unavoidable blocking work away from event-loop threads.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [16 · Cold sources, sharing, and sinks](../16-hot-cold-and-sinks/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
