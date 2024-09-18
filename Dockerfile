FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/hero-randomizer-api*.jar /app/hero-randomizer-api.jar

ENTRYPOINT ["java", "-jar", "/app/hero-randomizer-api.jar"]