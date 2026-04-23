# syntax=docker/dockerfile:1.7

FROM maven:3.9-eclipse-temurin-17 AS build
ARG SERVICE_PATH
WORKDIR /workspace
COPY pom.xml .
COPY backend ./backend
RUN --mount=type=cache,id=inventory-maven,target=/root/.m2,sharing=locked \
    mvn -s /workspace/backend/maven-settings.xml \
    -Dmaven.wagon.http.retryHandler.count=3 \
    -pl ${SERVICE_PATH} -am clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
ARG SERVICE_PATH
ARG SERVICE_PORT
WORKDIR /app
COPY --from=build /workspace/${SERVICE_PATH}/target/*.jar app.jar
EXPOSE ${SERVICE_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]
