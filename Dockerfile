# Use an official OpenJDK runtime as a parent image
FROM gradle:7.6-jdk17 AS builder
COPY . /app
# Set the working directory
WORKDIR /app
RUN gradle build -x test
# Copy the jar file (this will be created during build)

FROM openjdk:17-jdk-slim
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
