# Use Eclipse Temurin JRE on Alpine for the smallest, most secure attack surface
FROM eclipse-temurin:21-jre-alpine

# Create a non-privileged user for security compliance
RUN addgroup -S fintech && adduser -S fintech -G fintech
USER fintech
WORKDIR /app

# Copy the JAR downloaded by the CI pipeline
COPY target/fees-engine-core-1.0.0-SNAPSHOT.jar app.jar

# 2026 Performance Tuning: 
# 1. Generational ZGC for sub-ms pauses 
# 2. Virtual Threads enabled
# 3. Explicit RAM limits for Cgroup v2
ENTRYPOINT ["java", \
            "-XX:+UseZGC", \
            "-XX:+ZGenerational", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Dspring.threads.virtual.enabled=true", \
            "-jar", "app.jar"]

# Standardized health check for Kubernetes/AWS ECS
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:8084/actuator/health | grep UP || exit 1
