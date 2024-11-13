FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/hero-randomizer-api*.jar /app/hero-randomizer-api.jar
ENTRYPOINT ["java", "-jar", "/app/hero-randomizer-api.jar"]
