# Hero Randomizer API

Hero Randomizer API is a Kotlin-based REST API that generates random hero characters with various stats, weapons, armor, and backgrounds. 

## Features

- Generate random hero characters with completely random stats.
- Random assignment of weapons, armor, and accessories.
- Randomly generated background stories for each hero.

## Technologies

- **Kotlin** (1.9.25)
- **Spring Boot** (3.3.3)
- **Gradle** (Kotlin DSL)

## Endpoints
- `GET /hero` - Generate a random hero.

## Getting Started

### Prerequisites

- JDK 21+
- Gradle or use the Gradle Wrapper included in the project

### Running the Application

To run the application using Gradle:
```bash
./gradlew bootRun
```

Get Random Hero:
```bash
curl -X GET "http://localhost:8000/hero/new"
```

Or you can find by blueprint:
```bash
curl -X GET "http://localhost:8000/hero?blueprint=0WM0Q0AIQhDF3ofgifC_otZLjGmFFJoiaKwhTPEUB8YJpFd26jAdQlqXWNbppBix2SfWqXy8twf_3xo_eEHtXVYvnEAAAA"
```
