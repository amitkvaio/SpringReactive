# 13 · Server-Sent Events

> Send updates as they arrive instead of waiting for a complete list.

## Start with a familiar example

- A customer sees delivery updates one by one as the order moves forward.

## Learn the idea and follow the code

- SSE keeps one HTTP response open for server-to-client events.
- `delayElements` schedules a pause without sleeping on the request thread.
- `ServerSentEvent` carries an event name, ID, and data.
- The framework subscribes once for each connected request.
- Cancelling the response cancels the timer chain in this example.

- Open [TrackingController.java](src/main/java/dev/learning/TrackingController.java) and follow the method names in the trace below.

```java
Flux.just("CREATED", "PACKED", "DELIVERED")
    .delayElements(Duration.ofSeconds(1));
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [12-webclient](../12-webclient/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [TrackingController.java](src/main/java/dev/learning/TrackingController.java) — Finite timed SSE stream.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) — HTTP streaming and cancellation checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. GET /events selects the SSE route.
2. WebFlux subscribes to events().
3. The source sends CREATED, PACKED, and DELIVERED one second apart.
4. The finite stream completes after the third event.

## Run it locally

- Open a terminal in `13-server-sent-events`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8083/events after starting the server.

## Expected result

```text
event:delivery
data:CREATED
... PACKED ...
... DELIVERED ...
```

## Test it

```powershell
.\mvnw.cmd test
```

```bash
sh mvnw test
```

- The tests check the useful behavior rather than relying on console output.
- Press Ctrl+C to stop the server.

**Watch events in either terminal**

```powershell
curl.exe -N http://localhost:8083/events
```

```bash
curl -N http://localhost:8083/events
```

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Add an OUT_FOR_DELIVERY stage.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Add the stage before DELIVERED and update the SSE test.

</details>

## Interview questions and FAQs

**Is SSE bidirectional?**

- No, SSE sends server events to a client while WebSocket supports messages in both directions.

**Can I collectList before returning SSE?**

- That waits for completion and defeats incremental delivery.

**Does an event ID guarantee replay?**

- No, the server must implement replay storage and Last-Event-ID handling.

**Does this stream survive a restart?**

- No, the lesson generates a fresh finite sequence for each connection.

## Remember this

- Send updates as they arrive instead of waiting for a complete list.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [14 · Demand, buffering, and dropping](../14-backpressure/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
