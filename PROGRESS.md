# Course progress

- ROADMAP.md records the complete learning order.
- Lessons 01–26 and the two-service lesson 27 have been created.
- Every practical project includes a standalone POM, official Maven Wrapper, code, and tests.
- Lesson guides include a familiar example, code links, commands, expected results, an exercise, and interview FAQs.

## Verified on 2026-11-02

- The root Maven Wrapper completed `verify` successfully across all 28 runnable projects and both aggregators.
- All 80 routine tests passed with zero failures, errors, or skipped tests.
- All 6 real database integration tests passed with zero failures, errors, or skipped tests.
- PostgreSQL lesson 17 passed its order-and-audit save and transaction rollback checks.
- MongoDB lesson 18 passed its real document save-and-read check.
- The capstone passed real order-and-audit persistence, rollback, and optimistic-lock conflict checks.
- All 15 console demonstrations produced their documented results and exited without lingering scheduler warnings.
- The real RSocket TCP client returned `hello student`.
- The WebClient application's documented success, missing-delivery, and timeout URLs returned their expected text.
- The capacity lesson's PowerShell script completed 20 local requests and its work counter reflected those requests.
- HTTP tests verified REST validation, routing, authentication, context propagation, capacity rejection, SSE, and WebSocket exchange.
- The complete capstone Compose stack built and ran successfully with PostgreSQL and both services.
- The capstone HTTP walkthrough verified authentication, invalid input, request correlation, ETA lookup, filtering, missing IDs, and invalid transitions.
- Its live SSE stream emitted CREATED, PACKED, OUT_FOR_DELIVERY, and DELIVERED before completing.
- Stopping the delivery service produced ETA_PENDING while the saved order remained DELIVERED.
- The delivery service was restarted and its health endpoint returned UP.
- All 30 READMEs passed relative-link checks and include local commands, expected results, and interview FAQs.
- All project POMs parsed correctly and all copied wrappers match the official root wrapper.
- Source checks found no blocking terminal calls or manual subscriptions in HTTP handlers and services.
- Unix wrapper shell syntax passed using the installed Git shell.

## Validation commands and environment

- Java 24.0.2 ran Maven builds with compiler release 17.
- The capstone containers ran the packaged applications on the Java 17 runtime from their Dockerfiles.
- The wrapper downloaded and ran Maven 3.9.9 successfully.
- The root command was `.\mvnw.cmd -B -ntp -Dmaven.repo.local=D:/git/SpringReactive/target/maven-repository -fae verify`.
- Lesson 17 integration used `mvn -f 17-r2dbc-postgres/pom.xml -Pintegration failsafe:integration-test failsafe:verify` after compiling its test classes.
- Lesson 18 integration used `mvn -f 18-reactive-mongodb/pom.xml -Pintegration verify`.
- Capstone integration used `mvn -f 27-order-tracking-capstone/order-service/pom.xml -Pintegration verify`.
- Each Maven command used the same workspace-local dependency cache during validation.
- Docker Desktop was started to run the real infrastructure checks.
- Dependency resolution and Docker execution needed access outside this environment's restricted sandbox.
- If approval prompts recur, allow the required Maven build, wrapper, Maven Central lookup, and local Docker validation commands in `~/.Codex/settings.json`.
- No global settings file was edited.

## Running classroom demo

- The capstone is running at http://localhost:8091/orders with username `student` and disposable password `local-demo-only`.
- Its public health endpoint is http://localhost:8091/actuator/health.
- The local delivery stub is available at http://localhost:8092/delivery/1.
- Compose uses project name `springreactive27` so the capstone README commands manage this running stack.
- Stop it from the repository root with `docker compose -f 27-order-tracking-capstone/compose.yaml down`.
- Temporary lesson 17 and lesson 18 database containers were stopped while their named volumes were retained.
- Integration checks create teaching fixtures, so the next generated order ID may be greater than one.
- The walkthrough correctly asks students to use the generated ID rather than assuming a fresh database.

## Remaining limits

- Native macOS and Linux execution was not performed on this Windows machine.
- The capstone reports polled state snapshots rather than durable replayable event history.
- External tracing exporters, an external circuit breaker, and BlockHound instrumentation are documented extensions rather than installed integrations.
- No cloud deployment, paid API, git push, or image push was performed.
