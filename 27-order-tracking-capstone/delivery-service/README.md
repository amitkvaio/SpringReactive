# Capstone · Local delivery service

> Provide a local delivery response without a paid external API.

## Start with a familiar example

- A delivery desk gives the shop an estimated arrival time.

## Learn the idea and follow the code

- This independently runnable HTTP service is a deterministic local stub.
- The order service calls it using WebClient.
- It does not save orders or simulate a real courier network.

- Open [DeliveryController.java](src/main/java/dev/learning/DeliveryController.java) and follow the method names in the trace below.

```java
return Mono.just(Map.of("orderId", id, "etaMinutes", 15, "provider", "local-stub"));
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Read [the capstone walkthrough](../README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [DeliveryController.java](src/main/java/dev/learning/DeliveryController.java) — Local delivery stub.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) — HTTP response check.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. GET /delivery/1 enters DeliveryController.
2. WebFlux subscribes to the returned Mono.
3. The response contains orderId 1 and etaMinutes 15.

## Run it locally

- Open a terminal in `27-order-tracking-capstone/delivery-service`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8092/delivery/1 after starting the server.

## Expected result

```text
GET /delivery/1 -> {"orderId":1,"etaMinutes":15,"provider":"local-stub"}
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

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Return a different ETA.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change etaMinutes to 20 and update its HTTP assertion.

</details>

## Interview questions and FAQs

**Why keep this as a separate service?**

- It demonstrates a real local HTTP boundary while remaining independently runnable.

**Does this need PostgreSQL?**

- No, this stub can start on its own without any database.

## Remember this

- Provide a local delivery response without a paid external API.
- [Back to the learning roadmap](../../ROADMAP.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
