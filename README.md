# Hero Randomizer API

Hero Randomizer API is a Kotlin-based REST API that generates random hero characters with various stats, weapons, armor, and backgrounds.

## Features

- Generate random hero characters with completely random stats.
- Random assignment of weapons, armor, and accessories.
- Randomly generated background stories for each hero.

## Technologies

- **Kotlin** (1.9.25)
- **Spring Boot** (3.3.3)
- **Maven** (Wrapper included)

## Endpoints
- `GET /api/hero/new` - Generate a random hero.
- `GET /api/hero?blueprint=` - Get specific hero.

## Getting Started

### Prerequisites

- JDK 21+
- Maven or use the Maven Wrapper included in the project
- Docker (if you want to run the app using Docker)

### Running the Application

To run the application using Maven:
```bash
./mvnw spring-boot:run
```

To build, run, and clean the application using `make`:

Build the application and Docker image:
```bash
make build
```

Run the application:
```bash
make run
```

Clean the project removing the `target` directory, Docker image, and container:
```bash
make clean
```
### Interacting with the API

Get a Random Hero or find a hero by blueprint by sending HTTP requests to the appropriate endpoints.

Get Random Hero:
```bash
curl -X GET "http://localhost:8080/api/hero"
```

Or you can find by blueprint:
```bash
curl -X GET "http://localhost:8080/api/hero?blueprint=0XKyQ3AMAwDwYb2oVty_43FiQ0EemgIMgqTRYgzlNNNOhr0YjY6sBoaUyYpJVnDKNGUnKINF2xwtvKV7F3iL-xkYQ_vV8tP_9mtHthlwk-Q"
```
