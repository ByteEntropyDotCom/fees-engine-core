# Using a 2026-standard Eclipse Temurin Alpine image for small footprint
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S fintech && adduser -S fintech -G fintech
USER fintech

COPY target/fees-engine-core-1.0.0-SNAPSHOT.jar app.jar

# Optimize for Virtual Threads and Generational ZGC
ENTRYPOINT ["java", \
            "-XX:+UseZGC", \
            "-XX:+ZGenerational", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Dspring.threads.virtual.enabled=true", \
            "-jar", "/app.jar"]