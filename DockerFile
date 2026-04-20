# Stage 1: Build the application using Maven
FROM maven:3.8.4-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight JRE
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]