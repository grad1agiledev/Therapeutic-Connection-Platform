# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file (this will be created during build)
COPY build/libs/*.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
