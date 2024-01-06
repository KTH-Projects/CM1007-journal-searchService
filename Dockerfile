FROM adoptopenjdk:21-jdk-hotspot as builder

EXPOSE 8083

WORKDIR /app

COPY . .

RUN ./mvnw package

FROM adoptopenjdk:21-jdk-hotspot

COPY --from=builder /app/target/quarkus-app .

# If your jar file is not a runnable jar, you may need to adjust this command
CMD ["java", "-jar", "quarkus-run.jar"]
