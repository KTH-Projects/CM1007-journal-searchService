# Use OpenJDK JRE base image
FROM openjdk:17-jdk-slim as runtime
EXPOSE 8083
# Set the working directory in the Docker container
WORKDIR /app

# Copy the jar file and dependencies directory into the container
COPY target/quarkus-run.jar /app/
COPY target/lib/ /app/lib/

# Run the JAR file
CMD ["java", "-jar", "quarkus-run.jar"]
