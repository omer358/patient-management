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

> Prerequisites: Docker + Docker Compose, Java 17

1. Clone the repository
2. Start the services: