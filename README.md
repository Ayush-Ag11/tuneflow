# 🎵 TuneFlow

A production-grade Spotify-inspired music streaming platform built using **Java 21**, **Spring Boot 3**, and a *
*Microservices Architecture**.

TuneFlow is designed to demonstrate real-world backend engineering practices including authentication, API gateway
routing, distributed services, object storage, event-driven architecture, and scalable media management.

---

## 🚀 Project Overview

TuneFlow aims to replicate the core backend capabilities of modern music streaming platforms such as Spotify:

- User Authentication & Authorization
- User Profile Management
- Artist Management
- Album Management
- Track Management
- Genre Management
- Music Metadata Storage
- Media Asset Storage
- API Gateway Routing
- Event-Driven Communication
- Playlist Management *(In Progress)*
- Search Service *(Planned)*
- Streaming Service *(Planned)*
- Recommendation Engine *(Planned)*

---

## 🏗️ Microservices Architecture

```text
                         ┌─────────────────┐
                         │  Client / UI    │
                         └────────┬────────┘
                                  │
                                  ▼
                     ┌────────────────────────┐
                     │    Gateway Service     │
                     └────────┬───────────────┘
                              │
         ┌────────────────────┼────────────────────┐
         ▼                    ▼                    ▼
 ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
 │ Auth Service │    │ User Service │    │ Music Service│
 └──────────────┘    └──────────────┘    └──────────────┘
                                                    │
                                                    ▼
                                            ┌────────────┐
                                            │   MinIO    │
                                            └────────────┘

                              ▼
                         Apache Kafka
```

---

## 📦 Current Services

### 🔐 Auth Service (Port 8082)

Responsible for:

- User Registration
- User Login
- JWT Authentication
- Role-Based Authorization
- Token Validation
- Security Configuration

---

### 👤 User Service (Port 8083)

Responsible for:

- User Profile Management
- User Details Retrieval
- User Information Updates
- Service-to-Service Communication

---

### 🎶 Music Service (Port 8084)

Responsible for:

- Artist Management
- Album Management
- Track Management
- Genre Management
- Media Upload Management
- MinIO Integration
- Metadata Storage

---

### 🌐 Gateway Service (Port 8081)

Responsible for:

- API Routing
- Request Filtering
- JWT Validation
- Service Security

---

## 🛠️ Technology Stack

### Backend

- Java 21
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Cloud Gateway
- OpenFeign
- JWT Authentication
- Lombok

### Database

- PostgreSQL

### Storage

- MinIO Object Storage

### Messaging

- Apache Kafka

### Caching

- Redis

### DevOps

- Docker
- Docker Compose

---

## 📂 Project Structure

```text
TuneFlow
│
├── gateway-service
├── auth-service
├── user-service
├── music-service
│
├── playlist-service      (Planned)
├── search-service        (Planned)
├── streaming-service     (Planned)
│
├── common-lib
├── Infrastructure
└── docs
```

---

## ⚙️ Infrastructure Setup

### Start Infrastructure

Navigate to:

```bash
cd Infrastructure
```

Run:

```bash
docker-compose up -d
```

This starts:

- PostgreSQL
- Redis
- MinIO
- Kafka
- Kafka UI

---

## 🔧 Service Ports

| Service         | Port |
|-----------------|------|
| Gateway Service | 8081 |
| Auth Service    | 8082 |
| User Service    | 8083 |
| Music Service   | 8084 |
| Kafka UI        | 8080 |
| MinIO API       | 9000 |
| MinIO Console   | 9001 |
| PostgreSQL      | 5433 |

---

## 🗄️ Infrastructure Components

### PostgreSQL

Used for storing:

- Users
- Artists
- Albums
- Tracks
- Genres

---

### Redis

Used for:

- Caching
- Session Management
- Performance Optimization

---

### Kafka

Used for:

- Event-Driven Communication
- Asynchronous Processing
- Future Notification & Streaming Events

---

### MinIO

Used for storing:

- Artist Images
- Album Covers
- Track Assets

---

## 🔒 Security Features

- JWT-Based Authentication
- Stateless Authorization
- Spring Security Integration
- Protected APIs
- Gateway-Level Validation

---

## 📈 Features Implemented

### Authentication

- User Registration
- Login
- JWT Generation
- Token Validation

### Music Management

- Create Artist
- Update Artist
- Upload Artist Image
- Create Album
- Upload Album Cover
- Create Track
- Upload Track Assets
- Genre Management

### User Management

- User Profile APIs
- User Details Retrieval
- User Updates

---

## 🚧 Roadmap

### Phase 1 ✅

- Gateway Service
- Auth Service
- User Service
- Music Service
- PostgreSQL Integration
- MinIO Integration

### Phase 2 🚧

- Playlist Service
- Search Service
- Redis Caching
- Kafka Events

### Phase 3 📋

- Streaming Service
- Recommendation Engine
- Analytics Service
- Listening History
- Like & Follow Features

### Phase 4 📋

- Production Deployment
- Kubernetes
- Monitoring & Observability
- CI/CD Pipeline
- Distributed Tracing

---

## 🎯 Learning Objectives

This project focuses on building hands-on expertise in:

- Microservices Architecture
- Spring Boot Ecosystem
- API Gateway Patterns
- Distributed Systems
- Event-Driven Design
- Object Storage
- Containerization
- Production-Ready Backend Development

---

## 🤝 Contributing

Contributions, suggestions, and feedback are welcome.

Feel free to fork the repository, raise issues, and submit pull requests.

---

## 📜 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

**Ayush Agnihotri**

Backend Engineer | Java Developer

Building production-grade systems with:

- Java
- Spring Boot
- Microservices
- Kafka
- Redis
- PostgreSQL
- Docker

⭐ If you find this project useful, consider giving it a star.
