# Multi-stage build for optimized final image
FROM maven:3.8.6-eclipse-temurin-11 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Final lightweight production image
FROM eclipse-temurin:17-jre-jammy

# Security & configuration
ENV SPRING_PROFILES_ACTIVE=production \
    TZ=UTC \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseZGC -Djava.security.egd=file:/dev/./urandom"

# Configure non-root user and timezone
RUN groupadd --system --gid 1000 appgroup && \
    useradd --system --uid 1000 --gid appgroup appuser && \
    mkdir -p /app && \
    chown appuser:appgroup /app && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
USER appuser

COPY --from=builder --chown=appuser:appgroup /build/target/*.jar app.jar

# Security hardening
RUN find /app -type d -exec chmod 750 {} \; && \
    find /app -type f -exec chmod 640 {} \; && \
    chmod 755 /app/app.jar

EXPOSE 8080

# Enhanced health check with connection timeout
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -fsS --connect-timeout 2 http://localhost:8080/actuator/health || exit 1

# Secure entrypoint with exec form
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["${JAVA_OPTS}"]