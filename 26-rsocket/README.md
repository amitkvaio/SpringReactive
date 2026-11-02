# 26 · RSocket request-response · Optional

> Call a routed reactive service over an RSocket transport.

## Start with a familiar example

- A service desk receives a named request and replies with one greeting.

## Learn the idea and follow the code

- RSocket supports reactive interaction models over a transport such as TCP.
- `@MessageMapping("greet")` selects the route.
- RSocketRequester sends the name and receives a Mono reply.
- This optional lesson uses TCP port 7000 and does not expose a REST greeting endpoint.

- Open [GreetingController.java](src/main/java/dev/learning/GreetingController.java) and follow the method names in the trace below.

```java
@MessageMapping("greet")
Mono<String> greet(@Payload String name) { return Mono.just("hello " + name); }
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [24-observability-and-capacity](../24-observability-and-capacity/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [GreetingController.java](src/main/java/dev/learning/GreetingController.java) — RSocket route.
- [DemoClient.java](src/main/java/dev/learning/DemoClient.java) — Runnable TCP client.
- [GreetingTest.java](src/test/java/dev/learning/GreetingTest.java) — Handler behavior check.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. Start Application to listen on TCP 7000.
2. Run DemoClient in a second terminal.
3. The greet route receives student and returns hello student.

## Run it locally

- Open a terminal in `26-rsocket`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

## Expected result

```text
DemoClient -> hello student
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

**Run the client in a second terminal**

```powershell
.\mvnw.cmd compile exec:java
```

```bash
sh mvnw compile exec:java
```

- The automated test checks the greeting handler directly.
- The client command checks the actual TCP route when the server is running.
- HTTP port 8089 has no lesson route and is not the RSocket transport port.

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Change the client name.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change the data value from student to your name and run the client again.

</details>

## Interview questions and FAQs

**Is RSocket the same as REST?**

- No, it has interaction models and routing over transports such as TCP rather than ordinary REST resource requests.

**Can I call TCP RSocket with curl?**

- No, use an RSocket client such as the included RSocketRequester example.

**Why does the client use block()?**

- It is a finite command-line boundary rather than a reactive HTTP request handler.

## Remember this

- Call a routed reactive service over an RSocket transport.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [27 · Order and delivery tracking](../27-order-tracking-capstone/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
