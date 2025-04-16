# Multi-stage build for smaller final image
FROM maven:3.9-jdk-11 as builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Final image
FROM openjdk:17-jre-slim

# Security & configuration
ENV SPRING_PROFILES_ACTIVE=production \
    TZ=UTC \
    JAVA_OPTS="-Xms256m -Xmx512m"

RUN useradd -m appuser && \
    mkdir -p /app && \
    chown appuser:appuser /app && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app
USER appuser

COPY --from=builder --chown=appuser:appuser /build/target/*.jar app.jar

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]