# ---- Build Stage ----
    FROM maven:3.9.7-eclipse-temurin-21 AS build
    WORKDIR /app
    
    COPY . .
    RUN mvn clean package -DskipTests
    
    # ---- Run Stage ----
    FROM eclipse-temurin:21-jre
    WORKDIR /app
    
    COPY --from=build /app/target/helpdesk-0.0.1-SNAPSHOT.jar app.jar
    
    EXPOSE 8082
    ENTRYPOINT ["java", "-jar", "app.jar"]