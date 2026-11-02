# Spring Reactive learning roadmap

- Follow the required lessons in order.
- Optional lessons add another transport or database without blocking the main course.
- Each folder is an independent Maven project with its own wrapper.
- Java 17 or newer and basic Java knowledge are required throughout.

## Beginner — understand the sequence first

| # | Topic and folder | Prerequisite | Problem and familiar example | Build and learning goal | Required? |
|---|---|---|---|---|---|
| 01 | `01-reactive-fundamentals` | Java | A waiter should not wait at one table | Compare imperative work with deferred reactive work and distinguish execution terms | Yes |
| 02 | `02-reactive-streams` | 01 | A student asks for two worksheets at a time | Control demand with Publisher, Subscriber, Subscription, and Processor | Yes |
| 03 | `03-mono-and-flux` | 02 | A shop returns one receipt or several products | Model values, empty results, errors, and completion | Yes |
| 04 | `04-publishers-and-lifecycle` | 03 | A recipe runs only when someone cooks | Create lazy publishers and observe subscription and cancellation | Yes |
| 05 | `05-map-and-filter` | 04 | Keep affordable products and label them | Transform and filter a product sequence | Yes |
| 06 | `06-flatmap-and-ordering` | 05 | Order lookups finish at different times | Compose publishers and compare concurrency with output order | Yes |
| 07 | `07-combining-publishers` | 06 | Combine a customer with an order | Compare zip, merge, and concat | Yes |
| 08 | `08-error-handling` | 07 | A missing menu needs a fallback | Replace or translate terminal errors | Yes |
| 09 | `09-stepverifier` | 08 | Check each item before accepting a parcel | Test values, demand, errors, and cancellation | Yes |

## Intermediate — build HTTP applications

| # | Topic and folder | Prerequisite | Problem and familiar example | Build and learning goal | Required? |
|---|---|---|---|---|---|
| 10 | `10-webflux-rest` | 09, Spring Boot | Accept a valid order at a shop counter | Build annotated REST endpoints with validation and error responses | Yes |
| 11 | `11-functional-endpoints` | 10 | A receptionist routes a request to a handler | Separate routing from handling | Yes |
| 12 | `12-webclient` | 11 | Ask a nearby delivery desk for an ETA | Call a local HTTP stub with status handling and timeouts | Yes |
| 13 | `13-server-sent-events` | 12 | Send a customer live delivery updates | Stream SSE events and observe cancellation | Yes |
| 14 | `14-backpressure` | 09 | A receiver can carry only two boxes | Compare demand, batching, bounded buffering, and dropping | Yes |
| 15 | `15-schedulers-and-blocking` | 14 | Put a slow legacy task on a separate worker | Compare publishOn and subscribeOn and isolate blocking work | Yes |
| 16 | `16-hot-cold-and-sinks` | 15 | Compare a fresh recording with a live announcement | Share, cache, replay, and emit through sinks | Yes |
| 17 | `17-r2dbc-postgres` | 10, Docker | Save an order and its audit entry together | Use PostgreSQL, reactive transactions, pagination, and connection pools | Yes |
| 18 | `18-reactive-mongodb` | 10, Docker | Store flexible product cards | Use the reactive MongoDB repository alternative | Optional |
| 19 | `19-reactive-security` | 10 | A staff door needs a valid pass | Secure a reactive HTTP API | Yes |

## Advanced — control resources and failures

| # | Topic and folder | Prerequisite | Problem and familiar example | Build and learning goal | Required? |
|---|---|---|---|---|---|
| 20 | `20-context-and-event-loops` | 15 | Carry a ticket number across service desks | Propagate request correlation without assuming one thread | Yes |
| 21 | `21-debugging-and-cleanup` | 20 | Close a borrowed resource even after cancellation | Add checkpoints and clean up resources reliably | Yes |
| 22 | `22-time-and-retries` | 09, 21 | Retry a temporary failure with a deadline | Use virtual time, bounded backoff, and safe retry rules | Yes |
| 23 | `23-resilience` | 12, 22 | Limit calls when the delivery desk is overloaded | Bound concurrency and apply deadlines and fallbacks | Yes |
| 24 | `24-observability-and-capacity` | 20, 23 | Count slow requests before increasing capacity | Observe metrics, enforce limits, measure latency, and package deployment | Yes |
| 25 | `25-websockets` | 13 | Let a customer and agent exchange messages | Build a bidirectional WebSocket echo endpoint | Optional |
| 26 | `26-rsocket` | 12 | Ask another application for a routed reply | Build an RSocket request-response service | Optional |
| 27 | `27-order-tracking-capstone` | Required lessons 01–24 | Place an order and watch its delivery progress | Combine REST, R2DBC, transactions, WebClient, SSE, security, and correlation | Yes |

## Version-dependent extensions

- BlockHound is an optional blocking detector whose JVM instrumentation support must be checked before enabling it.
- Lesson 15 demonstrates blocking isolation without requiring a JVM instrumentation agent.
- An external circuit breaker library is an optional extension after checking its Reactor and Spring Boot compatibility.
- Lesson 23 uses supported Reactor operators for deadlines and concurrency limits without claiming to implement a circuit breaker.
- External tracing exporters and a durable message broker are optional production extensions.
- RSocket is optional and does not replace the required HTTP lessons.
