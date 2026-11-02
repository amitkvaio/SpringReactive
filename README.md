# Spring Reactive — Learn it, run it, teach it

> Start with small Java examples and finish with a reactive order-tracking application.

## Before you start

- Know basic Java before the console lessons and basic Spring Boot before the HTTP lessons.
- Use JDK 17 or newer and point `JAVA_HOME` to that JDK.
- The official Maven Wrapper downloads Maven 3.9.9 on its first run.
- Internet access is needed until Maven and dependencies are cached.
- Docker is needed only for database lessons and the full capstone.
- [ROADMAP.md](ROADMAP.md) lists objectives, prerequisites, examples, and optional topics.
- [VERSIONS.md](VERSIONS.md) records the selected versions and official sources.
- [TEACHING_GUIDE.md](TEACHING_GUIDE.md) gives a short classroom plan.
- [PROGRESS.md](PROGRESS.md) records actual validation and remaining setup.

## Run your first example

**Windows PowerShell**

```powershell
cd 01-reactive-fundamentals
.\mvnw.cmd test
.\mvnw.cmd compile exec:java
```

**macOS / Linux**

```bash
cd 01-reactive-fundamentals
sh mvnw test
sh mvnw compile exec:java
```

**Expected result**

```text
Demo: [order-1, order-2, order-3]
```

## Choose a lesson

- Lessons 01–09 introduce Reactor without a web server.
- Lessons 10–19 introduce HTTP, demand, schedulers, storage, and security.
- Lessons 20–26 cover context, debugging, resilience, operations, and optional transports.
- Lesson 27 combines the required ideas into one order-tracking demonstration.

| Lesson | Path |
|---|---|
| [01 · Why reactive programming?](01-reactive-fundamentals/README.md) | Required |
| [02 · Reactive Streams and demand](02-reactive-streams/README.md) | Required |
| [03 · Mono and Flux](03-mono-and-flux/README.md) | Required |
| [04 · Creation, laziness, and lifecycle](04-publishers-and-lifecycle/README.md) | Required |
| [05 · Map and filter](05-map-and-filter/README.md) | Required |
| [06 · FlatMap and ordering](06-flatmap-and-ordering/README.md) | Required |
| [07 · Zip, merge, and concat](07-combining-publishers/README.md) | Required |
| [08 · Errors and fallback values](08-error-handling/README.md) | Required |
| [09 · Test with StepVerifier](09-stepverifier/README.md) | Required |
| [10 · A small WebFlux REST API](10-webflux-rest/README.md) | Required |
| [11 · Functional routes and handlers](11-functional-endpoints/README.md) | Required |
| [12 · Call HTTP services with WebClient](12-webclient/README.md) | Required |
| [13 · Server-Sent Events](13-server-sent-events/README.md) | Required |
| [14 · Demand, buffering, and dropping](14-backpressure/README.md) | Required |
| [15 · Schedulers and blocking adapters](15-schedulers-and-blocking/README.md) | Required |
| [16 · Cold sources, sharing, and sinks](16-hot-cold-and-sinks/README.md) | Required |
| [17 · PostgreSQL with R2DBC](17-r2dbc-postgres/README.md) | Required |
| [18 · Reactive MongoDB · Optional](18-reactive-mongodb/README.md) | Optional |
| [19 · Reactive Spring Security](19-reactive-security/README.md) | Required |
| [20 · Reactor Context and event loops](20-context-and-event-loops/README.md) | Required |
| [21 · Checkpoints and resource cleanup](21-debugging-and-cleanup/README.md) | Required |
| [22 · Time, deadlines, and bounded retries](22-time-and-retries/README.md) | Required |
| [23 · Deadlines and bounded concurrency](23-resilience/README.md) | Required |
| [24 · Observe work and measure capacity](24-observability-and-capacity/README.md) | Required |
| [25 · WebSocket messages · Optional](25-websockets/README.md) | Optional |
| [26 · RSocket request-response · Optional](26-rsocket/README.md) | Optional |
| [27 · Order and delivery tracking](27-order-tracking-capstone/README.md) | Required |

## Project structure

- Each lesson has its own `pom.xml`, Maven Wrapper, source files, tests, and README.
- Console lessons have `Lesson.java` and `LessonTest.java` for small publisher demonstrations.
- HTTP lessons have an `Application.java` entry point and topic-specific handlers.
- Database lessons include local Compose configuration and separate integration checks.
- The capstone has independently runnable `order-service` and `delivery-service` folders.
- The root POM builds everything without becoming a parent dependency of the lessons.

## Local HTTP demonstrations

| Lesson | URL after starting its application |
|---|---|
| REST and validation | http://localhost:8080/orders/1 |
| Functional routes | http://localhost:8081/products |
| WebClient and local stub | http://localhost:8082/delivery/1 |
| SSE | http://localhost:8083/events |
| R2DBC and PostgreSQL | http://localhost:8084/orders |
| Security | http://localhost:8085/private |
| Context | http://localhost:8086/context |
| Capacity and metrics | http://localhost:8087/work |
| Optional MongoDB | http://localhost:8090/products |
| Capstone orders | http://localhost:8091/orders |
| Capstone delivery stub | http://localhost:8092/delivery/1 |

- Run the lesson's wrapper with `spring-boot:run` from its folder to start an HTTP example.
- Follow its README for credentials and database startup before sending requests.
- The WebSocket lesson uses `ws://localhost:8088/chat` with a WebSocket client.
- The RSocket lesson uses TCP port 7000 with its included client.
- Console lessons print finite output and do not provide a browser URL.
- Stop a directly started web application with Ctrl+C.

## Test the complete course

- Run these commands from the repository root.

```powershell
.\mvnw.cmd verify
```

```bash
sh mvnw verify
```

- Routine tests use StepVerifier, WebTestClient, mocks, and local stubs without requiring Docker.
- Database integration tests run explicitly with `-Pintegration verify` inside the relevant database project.
- The database README explains infrastructure startup, shutdown, and volume cleanup.
- The root build does not start a permanent classroom server.

## Teach it to a friend

1. Start with the lesson's problem and shop example.
2. Ask students to predict one result before running the code.
3. Follow one value or request through the linked source files.
4. Run the commands and compare actual output with the README.
5. Try the exercise before opening its folded solution.
6. Finish with the topic-specific interview questions.

## Interview FAQs

**Are asynchronous and non-blocking the same?**

- No, asynchronous work can still block the worker thread that executes it.

**Does returning Mono make JDBC non-blocking?**

- No, use a reactive driver or isolate unavoidable blocking calls on a bounded scheduler.

**Does flatMap create threads?**

- No, the sources and scheduler choices determine execution.

**Who subscribes in a WebFlux controller?**

- WebFlux subscribes to the publisher returned by the request handler.

**Is reactive programming always faster?**

- No, evaluate the workload and resource limits before choosing it.

**When is Spring MVC a reasonable choice?**

- MVC is often simpler when most application work uses blocking libraries.

**Can a lesson run without building earlier lessons?**

- Yes, practical projects depend on published libraries rather than another lesson's source code.

## Common setup problems

- Check `java -version` and `JAVA_HOME` when compilation fails.
- Check Maven Central or proxy access when dependencies cannot download.
- Stop the previous process if the required local port is occupied.
- Start Docker before database lessons and export the documented password variables.
- Do not copy terminal `block()` calls into HTTP request handlers.
- Do not expect an in-memory store or sink to survive a restart.
