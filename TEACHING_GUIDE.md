# Teach one small idea at a time

## Before class

- Run the lesson tests before sharing your screen.
- Start only the lesson's server and infrastructure.
- Keep the README beside the relevant source file.
- Use disposable credentials from the lesson guide.
- Read PROGRESS.md so unverified infrastructure is clear before the demo.

## Suggested sessions

| Session | Lessons | Show | Ask students |
|---|---|---|---|
| 1 | 01–04 | Immediate work and work triggered by subscription | Who subscribes and when does work start? |
| 2 | 05–07 | Prices, lookups, and ordering | Which operator preserves input order? |
| 3 | 08–09 | Fallbacks and subscriber signals | Does fallback restart the failed upstream? |
| 4 | 10–13 | A validated POST, local HTTP calls, and SSE | Who turns a publisher into an HTTP response? |
| 5 | 14–16 | Demand, thread names, and shared sources | Which resource is bounded? |
| 6 | 17 and optional 18 | Persistence and transactions | What can this transaction roll back? |
| 7 | 19–21 | Authentication, request IDs, and cleanup | Why can ThreadLocal lose request information? |
| 8 | 22–24 | Bounded retries and capacity metrics | Can retries make an outage worse? |
| 9 | Optional 25–26 | WebSocket and RSocket messages | Which interaction model fits? |
| 10 | 27 | Create and track an order | What is missing before production? |

## Use this lesson pattern

1. Read the familiar example aloud.
2. Ask students to predict one concrete result.
3. Point to the method receiving that input.
4. Follow the operators in the README trace.
5. Run the command and compare output with the prediction.
6. Change one input and run the focused tests.
7. Give the exercise before revealing its solution.
8. End with two interview questions from the lesson.

## Keep the terms clear

- Asynchronous means a result can arrive later and does not prove non-blocking I/O.
- Non-blocking means waiting does not hold the executing thread.
- Concurrency allows overlapping progress while parallelism means simultaneous execution.
- Mono and Flux describe publisher shapes rather than thread creation.
- Demand, buffers, pools, and remote services have different limits.
- WebFlux suits certain I/O workloads and is not an automatic speed upgrade.
- Reactive transactions cover only participating work in their reactive boundary.

## Capstone checkpoint

- Create a tea order and copy its generated ID.
- Request its ETA from the separate local delivery stub.
- Open SSE in a second terminal.
- Advance one status at a time with a pause so polling can observe each stage.
- Stop the delivery stub and show ETA_PENDING.
- Explain why the saved order remains when its later ETA lookup fails.
- Explain why current-state polling is not a durable event history.

## If a demonstration fails

- Check the exact URL and port from the lesson README.
- Confirm the required variables exist in the terminal starting the application.
- Check Docker before troubleshooting database code.
- Show the decisive failing test instead of guessing about thread behavior.
