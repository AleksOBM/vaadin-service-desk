# syntax=docker/dockerfile:1

# --- Этап сборки ---
# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml .
COPY serialization/pom.xml serialization/
COPY backend/pom.xml backend/
COPY frontend/pom.xml frontend/

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B dependency:go-offline -DexcludeArtifactIds=serialization -pl frontend -am

COPY . .

ENV NODE_OPTIONS --max-old-space-size=2048

RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=cache,target=/root/.npm \
    mvn -B clean package -DskipTests -pl frontend -am

FROM eclipse-temurin:25-jre-alpine

RUN apk add --no-cache curl

WORKDIR /app
COPY --from=build /app/frontend/target/frontend-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]