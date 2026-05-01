# Use Eclipse Temurin JRE on Alpine for the smallest attack surface
FROM eclipse-temurin:21-jre-alpine

# Security: Run as non-root user
RUN addgroup -S fintech && adduser -S fintech -G fintech
USER fintech
WORKDIR /app

# Copy the JAR from the CI build stage
COPY target/fees-engine-core-1.0.0-SNAPSHOT.jar app.jar

# 2026 Performance Tuning: 
# - Generational ZGC (Low latency)
# - Virtual Threads enabled
# - MaxRAMPercentage for container awareness
ENTRYPOINT ["java", \
            "-XX:+UseZGC", \
            "-XX:+ZGenerational", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Dspring.threads.virtual.enabled=true", \
            "-jar", "app.jar"]

# Health check for Kubernetes/Cloud Orchestrators
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:8084/actuator/health | grep UP || exit 1
