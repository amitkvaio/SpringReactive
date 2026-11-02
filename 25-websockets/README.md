# 25 · WebSocket messages · Optional

> Exchange messages in both directions over one connection.

## Start with a familiar example

- A customer and support agent can each start a new message without polling.

## Learn the idea and follow the code

- WebSocket upgrades an HTTP connection into a bidirectional message channel.
- The handler maps incoming text to an echo response.
- The returned send publisher keeps receive processing inside the session lifecycle.
- This optional example has no chat history, user accounts, or durable delivery.

- Open [SocketConfig.java](src/main/java/dev/learning/SocketConfig.java) and follow the method names in the trace below.

## Before you start

- Use JDK 17 or newer and set `JAVA_HOME` to your JDK folder.
- Complete [24-observability-and-capacity](../24-observability-and-capacity/README.md) first.
- The Maven Wrapper downloads Maven 3.9.9 on its first run.
- Maven needs internet access until dependencies are cached.

## Important files and dependencies

- [SocketConfig.java](src/main/java/dev/learning/SocketConfig.java) — WebSocket mapping and echo handler.
- [ApplicationTest.java](src/test/java/dev/learning/ApplicationTest.java) — Real socket exchange.
- [pom.xml](pom.xml) selects only this lesson's dependencies and uses Spring Boot dependency management.
- WebFlux supplies the reactive HTTP server and Reactor operators.
- Reactor Test supplies `StepVerifier` and JUnit runs the automated tests.

## Trace one example

1. A client connects to ws://localhost:8088/chat.
2. The mapping selects echoHandler.
3. Sending hello produces echo: hello.
4. Closing the connection cancels session work.

## Run it locally

- Open a terminal in `25-websockets`.

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
sh mvnw spring-boot:run
```

- Connect to `ws://localhost:8088/chat` with the browser-console example below.

## Expected result

```text
send: hello
receive: echo: hello
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

**Browser console demo**

```javascript
const socket = new WebSocket('ws://localhost:8088/chat');
socket.onmessage = event => console.log(event.data);
socket.onopen = () => socket.send('hello');
```

- Use the console on a local HTTP page that allows this connection.
- The normal HTTP root route returns 404 because this lesson exposes only the WebSocket path.

## Common mistakes and limits

- Check `JAVA_HOME` if Maven reports the wrong Java version.
- Stop the previous server if this port is already in use.
- Return the publisher because WebFlux subscribes to it for the HTTP response.
- Read the topic-specific FAQ before changing concurrency or error behavior.

## Try it yourself

- **Exercise:** Change the prefix to support.
- **Hint:** Change one operator or input and predict the result first.

<details>
<summary>Show the solution</summary>

- Change echo: to support: and update the socket test.

</details>

## Interview questions and FAQs

**How is WebSocket different from SSE?**

- WebSocket supports bidirectional messaging while SSE sends events from server to client.

**Does WebSocket store messages?**

- No, persistence and replay are application responsibilities.

**Can I open ws:// as a normal page?**

- Use a WebSocket client or the browser console rather than an HTTP address bar.

## Remember this

- Exchange messages in both directions over one connection.
- [Back to the learning roadmap](../ROADMAP.md).

- Next: [26 · RSocket request-response · Optional](../26-rsocket/README.md).

## Official references

- [Reactor reference](https://projectreactor.io/docs/core/release/reference/).
- [Spring WebFlux reference](https://docs.spring.io/spring-framework/reference/web/webflux.html).
