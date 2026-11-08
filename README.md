# Task Management System

## Project Overview
A RESTful task management system built for the HMCTS Developer Technical Test.
Users can create, view, update, and delete tasks through a simple API and frontend interface.

## Tech Stack

- Java 21
- Spring Boot
- H2 (in-memory database)
- Flyway (database migrations)
- Spring Data JPA
- JUnit 5 & Mockito
- Swagger Documentation

## How to Run
### Prerequisites
- Java 21+
- Maven

### Steps
1. Clone the repository
```bash
git clone https://github.com/Haris786Ali/task-management.git
```
2. Navigate into the project
```bash
cd task-management
```
3. Run the application
```bash
mvn spring-boot:run
```

App will start on `http://localhost:8081`

## Frontend

Access the frontend at:
`http://localhost:8081/index.html`

## API Documentation

Swagger UI is available at:
`http://localhost:8081/swagger-ui.html`

## Running Tests
```bash
mvn test
```