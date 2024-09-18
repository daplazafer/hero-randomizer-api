FROM gradle:jdk21

WORKDIR /app

COPY . /app

RUN gradle build -x test --no-daemon

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app/build/libs/hero-randomizer-api.jar"]