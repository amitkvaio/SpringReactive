# Versions and compatibility

- Verification date: 2026-11-02.
- Spring Boot 4.1.2 is the latest stable version confirmed from the official requirements page and Maven Central metadata during this task.
- Spring Boot 4.2.0-RC1 is a preview release and is intentionally excluded.
- Projects use Boot dependency management without overriding managed Spring, Reactor, Security, or Data versions.

| Item | Selection | Purpose |
|---|---|---|
| Java source release | 17 | Supported LTS baseline |
| Validation JDK | 24.0.2 | The installed local JDK |
| Spring Boot | 4.1.2 | Verified stable release |
| Spring Framework | 7.0.10 | Managed by the selected Boot BOM |
| Reactor Core | 3.8.8 | Managed through Reactor BOM 2025.0.8 |
| Spring Security | 7.1.2 | Managed by the selected Boot BOM |
| Spring Data BOM | 2026.0.2 | Aligns the reactive repository modules |
| PostgreSQL R2DBC driver | 1.1.3.RELEASE | Managed by the selected Boot BOM |
| JUnit Jupiter | 6.0.3 | Managed test platform |
| Maven | 3.9.9 | Pinned wrapper distribution |
| Maven Wrapper | 3.3.4 | Official script-only wrapper |
| Exec Maven Plugin | 3.5.0 | Finite Reactor console examples |
| PostgreSQL image | 17-alpine | Local relational infrastructure |
| MongoDB image | 8.0 | Optional document infrastructure |
| Container Java runtime | eclipse-temurin:17-jre | Matches the source baseline |

## Official sources

- [Spring Boot requirements](https://docs.spring.io/spring-boot/system-requirements.html) specify supported Java and Maven ranges.
- [Maven Central metadata](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-starter-parent/maven-metadata.xml) confirms published stable and preview artifacts.
- [Managed dependency coordinates](https://docs.spring.io/spring-boot/appendix/dependency-versions/coordinates.html) describe the managed library set.
- [Spring Boot starters](https://docs.spring.io/spring-boot/reference/using/build-systems.html) describe the HTTP, database, security, and test starters.
- [Reactor reference](https://projectreactor.io/docs/core/release/reference/) documents operators and scheduler behavior.
- [Reactive Streams](https://www.reactive-streams.org/) defines publisher demand and subscriber signals.
- [Reactive transactions](https://docs.spring.io/spring-framework/reference/data-access/transaction/programmatic.html) document TransactionalOperator.
- [WebFlux security](https://docs.spring.io/spring-security/reference/reactive/configuration/webflux.html) documents the reactive security chain.
- [Spring Boot metrics](https://docs.spring.io/spring-boot/reference/actuator/metrics.html) documents Micrometer integration.
- [Maven Wrapper](https://maven.apache.org/wrapper/) documents wrapper setup.
- [PostgreSQL container](https://hub.docker.com/_/postgres) describes container initialization.
- [MongoDB container](https://hub.docker.com/_/mongo) describes document database initialization.

## Compatibility limits

- Compilation targets Java 17 while actual local builds use the validation JDK listed above.
- Driver dependency resolution alone does not prove compatibility with a running database server.
- See PROGRESS.md for the database and Docker checks actually executed.
- Container tags receive patch updates and are not immutable image digests.
- RSocket and reactive MongoDB are optional extensions.
- BlockHound is not installed because instrumentation compatibility must be checked for the selected JDK.
- External circuit breakers, tracing exporters, and brokers need their own compatibility checks before adoption.
