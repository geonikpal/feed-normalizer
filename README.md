# Feed Normalizer

A Spring Boot service that ingests raw feed messages from multiple sports data providers and normalizes them into a unified internal format.

***

## Project Structure

```
feed-normalizer/
├── src/
│   └── main/
│       └── java/
│           └── com/sporty/feednormalizer/
│               ├── controller/        ← REST endpoints per provider
│               ├── model/             ← Normalized message model
│               ├── service/           ← Normalization logic per provider
│               ├── exception/         ← Global exception handler
│               └── FeedNormalizerApplication.java
├── logs/                              ← Auto-created by Docker volume
├── Dockerfile                         ← Builds the app image (Java 25)
├── Dockerfile.test                    ← Lightweight Alpine test runner
├── docker-compose.yml                 ← Orchestrates app + test runner
├── test-endpoints.sh                  ← Test script (Linux/Mac/Docker)
├── test-endpoints.bat                 ← Test script (Windows)
├── pom.xml
└── README.md
```

***
## Technologies

| Technology           | Version          | Purpose                                                               |
|----------------------|------------------|-----------------------------------------------------------------------|
| Java                 | 25               | Language                                                              |
| Spring Boot          | 4.0.5            | Application framework                                                 |
| Spring Web MVC       | 4.0.5            | REST API (`spring-boot-starter-webmvc`)                               |
| Spring Boot Actuator | 4.0.5            | Health check endpoint (`/actuator/health`)                            |
| Maven                | 3.8+             | Build tool                                                            |
| JUnit 5              | latest (managed) | Unit & integration testing                                            |
| Mockito              | latest (managed) | Mocking in tests                                                      |
| MockMvc              | 4.0.5            | HTTP layer integration testing (`spring-boot-starter-webmvc-test`)    |
***
## Getting Started

### Clone the repository

```bash
git clone https://github.com/geonikpal/feed-normalizer.git
cd feed-normalizer
```

---
## How to Run

### Option 1: Docker (Recommended - works on all platforms)

**Prerequisites:** Docker Desktop installed.

**Run the app:**
```bash
docker-compose up --build feed-normalizer
```

The service starts on **http://localhost:8080**.

**Run app + tests together:**
```bash
docker-compose up --build
```

**Run tests only (app already running):**
```bash
docker-compose run --rm test-runner
```

**Stop everything:**
```bash
docker-compose down
```

***

### Option 2: Maven

**Prerequisites:** Java 25+, Maven 3.8+

```bash
./mvnw clean install
./mvnw spring-boot:run
```
***

## How to Test

The service must be running before executing the test scripts.

### Mac / Linux
```bash
chmod +x test-endpoints.sh
./test-endpoints.sh
```

### Windows
```bat
./test-endpoints.bat
```
> Normalized messages are written to `logs/feed-normalizer.log`.

***

## Endpoints

| Provider      | Endpoint               | Method |
|---------------|------------------------|--------|
| ProviderAlpha | `/provider-alpha/feed` | POST   |
| ProviderBeta  | `/provider-beta/feed`  | POST   |

### ProviderAlpha - Example Payloads

**ODDS_CHANGE:**
```json
{
  "msg_type": "odds_update",
  "event_id": "ev123",
  "values": { "1": 2.0, "X": 3.1, "2": 3.8 }
}
```

**BET_SETTLEMENT:**
```json
{
  "msg_type": "settlement",
  "event_id": "ev123",
  "outcome": "1"
}
```

### ProviderBeta - Example Payloads

**ODDS_CHANGE:**
```json
{
  "type": "ODDS",
  "event_id": "ev456",
  "odds": { "home": 1.95, "draw": 3.2, "away": 4.0 }
}
```

**BET_SETTLEMENT:**
```json
{
  "type": "SETTLEMENT",
  "event_id": "ev456",
  "result": "away"
}
```

***

## Logs

Normalized messages are written to:
```
logs/feed-normalizer.log
```

When running via Docker, this folder is mounted as a volume so you can read it directly from your machine without entering the container.