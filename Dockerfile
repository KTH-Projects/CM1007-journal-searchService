# Use OpenJDK JRE base image
FROM openjdk:17-jdk-slim as runtime

# Set the working directory in the Docker container
WORKDIR /app

# Copy the entire 'quarkus-app' directory into the container
COPY target/quarkus-app/ /app/

# Run the JAR file
CMD ["java", "-jar", "quarkus-run.jar"]
