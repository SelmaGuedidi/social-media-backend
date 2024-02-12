FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml pom.xml
COPY .mvn .mvn
COPY src src

RUN mvn package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
