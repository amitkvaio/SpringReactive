# 12 · Call HTTP services with WebClient

> Ask another HTTP endpoint for a result without blocking the request thread.

## Start with a familiar example

- The shop asks its local delivery desk how long an order will take.

## Learn the idea and follow the code

- WebClient builds a reactive HTTP exchange.
- `retrieve` turns error statuses into failures unless a status handler changes them.
- `bodyToMono` decodes one response body.
- `timeout` cancels a stalled upstream subscription after one second.
- This application contains a local stub so no public API or paid service is needed.
- Changing server.port also requires changing delivery.base-url because the default stub URL uses 8082.

- Open [DeliveryClient.java](src/main/java/dev/learning/DeliveryClient.java) and follow the method names in the trace below.

```java
return client.get().uri("/stub/delivery/{id}", id).retrieve()
    .onStatus(status -> status.value() == 404, response -> Mono.error(new IllegalArgumentException("delivery not found")))
    .bodyToMono(String.class).timeout(Duration.ofSeconds(1));
```

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [11-functional-endpoints](../11-functional-endpoints/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [DeliveryClient.java](src/main/java/dev/learning/DeliveryClient.java) — HTTP composition and deadline.
- [DeliveryController.java](src/main/java/dev/learning/DeliveryController.java) — Public routes and local stub.
- [DeliveryClientTest.java](src/test/java/dev/learning/DeliveryClientTest.java) — Real local HTTP and virtual-time timeout checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- The WebClient starter supplies Spring Boot's configured WebClient.Builder bean.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) checks full application startup with its HTTP client configuration.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. GET /delivery/1 reaches DeliveryController.
2. DeliveryClient builds GET /stub/delivery/1.
3. WebFlux subscribes and WebClient performs the local HTTP exchange.
4. The remote text becomes the first HTTP response.

## Run it locally

- Open a terminal in `12-webclient`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8082/delivery/1 after starting the server.

## Expected result

```text
GET /delivery/1 -> ETA: 15 minutes
GET /delivery/missing -> unknown delivery
GET /delivery/slow -> ETA pending
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

- **Exercise:** Change the timeout fallback text.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change ETA pending in the controller and check /delivery/slow.

</details>

## Interview questions and FAQs

**Is WebClient automatically non-blocking?**

- Its reactive connector can use non-blocking I/O, but blocking callbacks still block their executing threads.

**What does cancellation do?**

- The client cancels its pending exchange where supported, but the remote server may already have performed work.

**Does a 404 always become an empty Mono?**

- No, retrieve treats error statuses as errors unless you customize that behavior.

**Why test with a local stub?**

- It checks real HTTP behavior without relying on internet availability or someone else’s service.

## Remember this

- Ask another HTTP endpoint for a result without blocking the request thread.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [13 · Server-Sent Events](../13-server-sent-events/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
