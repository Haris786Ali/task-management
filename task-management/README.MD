# Task Management System

## Project Overview
A RESTful task management system built for the HMCTS Developer Technical Test.
Users can create, view, update, and delete tasks through a simple API and frontend interface.

## Tech Stack

- Java 21
- Spring Boot
- H2 (in-memory database)
- Spring Data JPA
- JUnit 5 & Mockito
- Swagger Documentation

## How to Run
### Prerequisites
- Java 21+
- Maven

### Steps
1. Clone the repository
   git clone https://github.com/Haris786Ali/task-management.git
2. Navigate into the project
   cd task-management
3. Run the application
   mvn spring-boot:run

App will start on http://localhost:8081

## API Documentation

Swagger UI is available at:
http://localhost:8081/swagger-ui.html

## Running Tests

mvn test will run the full suite of tests