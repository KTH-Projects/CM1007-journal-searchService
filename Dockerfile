FROM eclipse-temurin:17-jdk-jammy as builder

EXPOSE 8083

WORKDIR /app

COPY . .

RUN ./mvnw package

FROM eclipse-temurin:17-jdk-jammy

COPY --from=builder /app/target/quarkus-app .

# If your jar file is not a runnable jar, you may need to adjust this command
CMD ["java", "-jar", "quarkus-run.jar"]
