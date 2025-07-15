FROM eclipse-temurin:21-jdk



WORKDIR /app



# Copy everything including the target folder

COPY . .



# Expose the port your app runs on

EXPOSE 8082



# Run the built JAR

ENTRYPOINT ["java", "-jar", "target/helpdesk-0.0.1-SNAPSHOT.jar"]