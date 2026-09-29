FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/maintainable-application-server-1.0.0.jar app.jar
ENV APP_ENV=production
ENV GREETING_PREFIX=Hello
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
