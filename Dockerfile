FROM maven:3.9.1-eclipse-temurin-17 AS build
COPY pom.xml /app/
WORKDIR /app/
RUN mvn dependency:go-offline
COPY . .
RUN mvn package -DskipTests

FROM openjdk:17-alpine
RUN mkdir -p /app/target/
COPY --from=build /app/target/*.jar /app/target/app.jar
ENTRYPOINT ["java", "-jar", "/app/target/app.jar"]
