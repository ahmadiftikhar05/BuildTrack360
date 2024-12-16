# Use an OpenJDK base image
FROM openjdk:22-jdk-slim

# Set the working directory
WORKDIR /app

# Copy your compiled JAR file into the container
COPY target/BuildTrack360-1.0-SNAPSHOT.jar app.jar

# Expose the port your application listens on (if applicable)
EXPOSE 8080

# Command to run your JavaFX application
CMD ["java", "-jar", "app.jar"]
