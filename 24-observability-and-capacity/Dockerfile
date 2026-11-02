FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/24-observability-and-capacity-1.0.0.jar app.jar
EXPOSE 8087
USER 10001
ENTRYPOINT ["java", "-jar", "app.jar"]
