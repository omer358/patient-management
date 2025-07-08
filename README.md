# 🏥 Patient Management System

A microservices-based patient management system demonstrating secure authentication, Kafka-based event streaming, gRPC
service communication, and API Gateway routing. Built with Spring Boot and Docker, the system is modular and follows
good architecture and integration practices.

## 🧩 Architecture Overview

This project follows a **microservices architecture** with the following services:

* **Auth Service**: Handles user authentication using JWT with role-based access control (ADMIN, DOCTOR, PATIENT).
* **Patient Service**: Provides CRUD operations for patient records and publishes Kafka events.
* **Billing Service**: Exposes a gRPC interface to process billing requests.
* **Analytics Service**: Consumes patient events from Kafka for analytics use.
* **API Gateway**: Routes requests to services and validates JWT tokens.

## 🚀 Tech Stack

| Layer          | Technologies                                           |
|----------------|--------------------------------------------------------|
| Language       | Java 17                                                |
| Frameworks     | Spring Boot 3.x, Spring Security, Spring Cloud Gateway |
| Database       | PostgreSQL                                             |
| Messaging      | Apache Kafka                                           |
| RPC            | gRPC + Protobuf                                        |
| Auth           | JWT (JSON Web Token)                                   |
| DevOps         | Docker, Docker Compose                                 |
| Infrastructure | LocalStack (for AWS emulation), AWS CDK (Java)         |
| Testing        | Integration tests with JUnit, REST Assured             |

---

## 🧠 Features

### ✅ Auth Service

* User registration and login with email/password
* Role-based authentication (ADMIN, DOCTOR, PATIENT)
* JWT token generation with role claims
* Token validation for downstream services
* Endpoints:
    * POST `/api/auth/login`
    * GET `/api/auth/validate`
* PostgreSQL for user data persistence

### ✅ Patient Service

* CRUD APIs for patients (`/api/patients`)
* Sends Kafka events upon patient creation/updates
* Calls Billing Service via gRPC for account setup
* Role-based access control integration

### ✅ Billing Service

* gRPC endpoint to create billing accounts
* Uses Protobuf for schema definition
* Secure service-to-service communication

### ✅ Analytics Service

* Kafka consumer that listens to patient events
* Basic event logging (scaffold for future insights)
* Real-time event processing

### ✅ API Gateway

* Central entry point (`/api/*`)
* JWT validation via custom filter before routing
* Route configuration for all microservices
* Security and rate limiting

---

## 🧪 Integration Tests

Under `integration-tests` module:

* End-to-end flow testing between services
* Authentication flow validation
* Role-based access control testing
* Kafka event verification
* gRPC communication testing

---

## 🐳 Running the Project

### Prerequisites

* Docker Desktop 4.x+ and Docker Compose v2
* Java Development Kit (JDK) 17
* Maven 3.8+
* Git
* At least 8GB RAM available for containers
* Ports 4000-4005, 5001, 9092 free on your machine

### Configuration Setup

1. Clone the repository:
   ```bash
    git clone https://github.com/yourusername/patient-management.git
    cd patient-management
   ```
   
2. Build all services:
   ```bash
    mvn clean package -DskipTests
   ```

### 🚀 Starting Services

1. **Build all services (if not already built):**

   ```bash
    docker compose build
   ```

2. **Start all services:**

   ```bash
    docker compose up -d
   ```

   This will start:

    * PostgreSQL for `auth-service` and `patient-service`.
    * Kafka.
    * All microservices (`auth-service`, `patient-service`, `billing-service`, `analytices-service`, `api-gateway`).

3. **(Optional) Start services individually for focused debugging:**

```bash
   docker compose up -d auth-service-db patient-service-db kafka
   docker compose up -d auth-service
   docker compose up -d patient-service
   docker compose up -d billing-service
   docker compose up -d analytices-service
   docker compose up -d api-gateway
  ```

---

### ✅ Verify Services Are Running

To verify containers are up:

```bash
  docker compose ps
```

Expected ports:

| Service              | Port        | Notes                    |
| -------------------- | ----------- | ------------------------ |
| PostgreSQL (auth)    | 5001        | `auth-service-db`        |
| PostgreSQL (patient) | 5000        | `patient-service-db`     |
| Kafka                | 9092        | Internal use only        |
| API Gateway          | 4004        | Entry point to system    |
| Auth Service         | 4005        | Login / Token validation |
| Patient Service      | 4000        | Patient management       |
| Billing Service      | 4001 / 9001 | gRPC + HTTP (if any)     |
| Analytics Service    | 4002        | Kafka consumer           |

You can also verify individual services by hitting their Swagger docs:

```
http://localhost:4004/api-docs/patients
```

## 🔄 CI/CD (Planned)

This project is designed to be CI/CD ready using GitHub Actions. Future enhancements include:
- Test and lint on push.
- Docker image build and push.
- Deployment.