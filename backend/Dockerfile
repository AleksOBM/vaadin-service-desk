# syntax=docker/dockerfile:1

# --- Этап сборки ---
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml .
COPY serialization/pom.xml serialization/
COPY backend/pom.xml backend/
COPY frontend/pom.xml frontend/

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B dependency:go-offline -DexcludeArtifactIds=serialization -pl backend -am

COPY . .

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B clean package -DskipTests -pl backend -am

# Скачиваем grpc_health_probe (через curl, с автоопределением арки)
ARG TARGETARCH
RUN if [ -z "$TARGETARCH" ]; then TARGETARCH="amd64"; fi && \
    curl -L -o /tmp/grpc_health_probe \
    https://github.com/grpc-ecosystem/grpc-health-probe/releases/download/v0.4.48/grpc_health_probe-linux-${TARGETARCH} && \
    chmod +x /tmp/grpc_health_probe

# --- Этап запуска ---
FROM eclipse-temurin:25-jre-alpine

COPY --from=build /tmp/grpc_health_probe /usr/local/bin/grpc_health_probe

WORKDIR /app
COPY --from=build /app/backend/target/backend-*.jar app.jar

EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]