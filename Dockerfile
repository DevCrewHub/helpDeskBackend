# ---- Build Stage ----
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# ---- Run Stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/helpdesk-0.0.1-SNAPSHOT.jar app.jar

# Allow environment variable overrides for DB config
# ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
# ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
# ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}

EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"] 