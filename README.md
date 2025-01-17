# Project Setup Instructions

This document provides the required steps to build, run, and test the project. Follow these instructions carefully to ensure the application works as expected.

---

## Prerequisites

### Tools and Environment
- **Java 11** (Ensure it is installed and added to your PATH.)
- **Maven 3.6+** (For dependency management and build process.)

### External Dependencies
The project is configured to use the H2 in-memory database by default for simplicity. No additional setup is required for the database unless you want to use an external one.

---

## Steps to Build the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/agilecontent/Agile-Java-backend-code-challenge.git
   cd Agile-Java-backend-code-challenge
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

This command will compile the source code, run unit tests, and package the application.

---

## Running the Application

1. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
2. Access the application at:
   ```
    http://localhost:8080/api/users
   ```
---

## Testing the Application

### Unit Tests
Run the unit tests with the following command:
```bash
mvn test
```

### API Testing

Once the application is running, you can use tools like Postman or `curl` to test the API endpoints.

#### Some Endpoints:

1. **Fetch paginated users**:
   ```
   GET /api/users?page=0&size=10
   ```

2. **Fetch user tree**:
   ```
   GET /api/users/tree
   ```

---
