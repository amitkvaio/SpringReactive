# 19 · Reactive Spring Security

> Allow a protected request only when credentials are valid.

## Start with a familiar example

- A staff member shows a pass before entering a restricted shop area.

## Learn the idea and follow the code

- SecurityWebFilterChain authorizes requests before the controller runs.
- MapReactiveUserDetailsService is an in-memory teaching user store.
- Passwords come from environment variables and are encoded with BCrypt at startup.
- This lesson uses HTTP Basic for local curl demonstrations and does not implement JWT issuance.

- Open [SecurityConfig.java](src/main/java/dev/learning/SecurityConfig.java) and follow the method names in the trace below.

```java
http.authorizeExchange(auth -> auth.pathMatchers("/actuator/health")
    .permitAll().anyExchange().authenticated());
```

## Before you start

- Set `DEMO_PASSWORD` using the credential commands below before starting the server.

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [17-r2dbc-postgres](../17-r2dbc-postgres/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [SecurityConfig.java](src/main/java/dev/learning/SecurityConfig.java) — Reactive authentication and authorization.
- [PrivateController.java](src/main/java/dev/learning/PrivateController.java) — Protected endpoint.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) — Authentication checks.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. GET /private reaches the security filters.
2. Missing or invalid credentials return 401.
3. Valid credentials allow the controller to return hello student.

## Run it locally

- Open a terminal in `19-reactive-security`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Open http://localhost:8085/private after starting the server.

## Expected result

```text
Without credentials -> HTTP 401
With student credentials -> {"message":"hello student"}
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

**Set local teaching credentials before the run command**

```powershell
$env:DEMO_USER = "student"
$env:DEMO_PASSWORD = "local-demo-only"
```

```bash
export DEMO_USER=student
export DEMO_PASSWORD=local-demo-only
```

- These are disposable classroom credentials rather than production secrets.
- An unauthenticated request returns HTTP 401.
- HTTP Basic over plain HTTP is suitable only for this local demo.
- CSRF protection is disabled for this stateless command-line API and must be reconsidered before adding browser credentials or cookies.

**Send an authenticated request**

```powershell
curl.exe -u student:local-demo-only http://localhost:8085/private
```

```bash
curl -u student:local-demo-only http://localhost:8085/private
```

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Require a different role for an admin route.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Add /admin and use pathMatchers("/admin").hasRole("ADMIN") before anyExchange().

</details>

## Interview questions and FAQs

**Why use SecurityWebFilterChain?**

- It is the reactive web security chain for WebFlux applications.

**Is a 401 the same as a 403?**

- A 401 means authentication is required or failed, while a 403 means an authenticated request lacks permission.

**Why read a password from the environment?**

- It keeps deployment credentials outside committed source files.

**Is disabling CSRF always safe for Basic authentication?**

- No, browsers may send Basic credentials automatically, so browser-facing applications need a suitable CSRF strategy.

## Remember this

- Allow a protected request only when credentials are valid.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [20 · Reactor Context and event loops](../20-context-and-event-loops/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
