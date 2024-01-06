# Use OpenJDK JRE base image
FROM openjdk:17-jdk-slim as runtime
EXPOSE 8083
# Set the working directory in the Docker container
WORKDIR /app

# Assuming that your build outputs a runnable jar file in the target directory
# If you have a 'target/quarkus-app' directory, adjust the paths accordingly
COPY target/quarkus-run.jar /app/

# If your application needs additional libraries from the target/lib directory, copy them as well
#COPY target/lib/ /app/lib/

# Run the JAR file
# If your jar file is not a runnable jar, you may need to adjust this command
CMD ["java", "-jar", "quarkus-run.jar"]
