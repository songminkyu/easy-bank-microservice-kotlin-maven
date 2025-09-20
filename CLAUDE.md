# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Building and Running
- **Build all services**: `mvn clean compile` (from root directory)
- **Build specific service**: `mvn clean compile` (from specific service directory like `apps/account/`)
- **Run service locally**: `mvn spring-boot:run` (from service directory)
- **Build Docker images**: `mvn jib:build` or `mvn jib:dockerBuild` (builds to local Docker)

### Testing
- **Run unit tests**: `mvn test`
- **Run integration tests**: `mvn failsafe:integration-test`
- **Run specific test**: `mvn test -Dtest=ClassName`
- **Code coverage**: `mvn jacoco:report` (generates reports in `target/site/jacoco/`)

### Code Quality
- **Checkstyle**: `mvn checkstyle:check` (Java code style validation)
- **Detekt**: `mvn antrun:run@detekt` (Kotlin code analysis)
- **Dependency convergence**: `mvn enforcer:enforce` (validates Maven dependencies)
- **SonarQube analysis**: `mvn sonar:sonar -Psonar`

### Infrastructure and Deployment
- **Start full development environment**:
  ```bash
  cd k8s/docker-compose/default
  docker-compose up -d
  ```
- **Start specific environment**: Replace `default` with `dev`, `qa`, or `prod`
- **Kubernetes deployment (Helm)**:
  ```bash
  helm install eazybank-dev k8s/helm/environments/dev-env
  ```

## Architecture Overview

This is a **microservices architecture** using Spring Boot and Spring Cloud, implementing a banking application with the following key patterns:

### Core Infrastructure Services
1. **Config Server** (port 8071): Centralized configuration management using Spring Cloud Config
2. **Eureka Server** (port 8070): Service discovery and registration
3. **Gateway Server** (port 8072): API Gateway with routing, filtering, and load balancing

### Business Microservices
1. **Account Service** (port 8080): Customer account management
2. **Loan Service** (port 8090): Loan processing and management
3. **Card Service** (port 9000): Card operations and services
4. **Message Service** (port 9010): Event-driven messaging and notifications

### Technology Stack
- **Languages**: Java 21, Kotlin 2.1 (mixed codebase)
- **Framework**: Spring Boot 3.3, Spring Cloud 2023.0.1
- **Build**: Maven 3.8.8 (parent POM at `apps/parent/pom.xml`)
- **Database**: PostgreSQL with Liquibase migrations, H2 for testing
- **Caching**: Redis with Redisson
- **Messaging**: Kafka with Spring Cloud Stream
- **Security**: OAuth2, JWT, Keycloak integration
- **Observability**: Prometheus metrics, Grafana dashboards, Loki logs, Tempo tracing

### Key Architectural Patterns
- **Event-Driven Architecture**: Uses Kafka for asynchronous communication
- **Circuit Breaker**: Resilience4j for fault tolerance
- **API Gateway Pattern**: Single entry point with request routing
- **Configuration Externalization**: Centralized config management
- **Database per Service**: Each microservice has its own database schema
- **Change Data Capture (CDC)**: Debezium for data synchronization

## Development Guidelines

### Service Startup Order
Services must be started in this order for proper dependency resolution:
1. Infrastructure (PostgreSQL, Redis, Kafka, Keycloak)
2. Config Server
3. Eureka Server
4. Business microservices (Account, Loan, Card, Message)
5. Gateway Server

### Multi-Language Codebase
- **Kotlin**: Primary language for business logic (`src/main/kotlin/`)
- **Java**: Used for configuration, utilities, and some components
- **MapStruct**: Entity-DTO mapping (annotation processing configured)

### Configuration Management
- **Local development**: `application-local.yml` profiles active by default
- **Environment-specific**: Configs in `k8s/microservice-config/` for different environments
- **Centralized configs**: Stored in Config Server (`central-server-config/`)

### Testing Strategy
- **Unit tests**: Located in `src/test/java/` (mixed Java/Kotlin)
- **Integration tests**: Files ending with `*IT*` or `*IntTest*`
- **Test separation**: Surefire for unit tests, Failsafe for integration tests

### Client Communication
- **Feign Clients**: For synchronous inter-service communication
- **GraphQL**: Some services expose GraphQL endpoints
- **WebClient**: Reactive HTTP client for external APIs
- **Kafka**: Asynchronous messaging between services

### Monitoring and Observability
- **Health checks**: Available at `/actuator/health`
- **Metrics**: Exposed at `/actuator/prometheus`
- **API Documentation**: Swagger UI accessible via Gateway
- **Distributed tracing**: OpenTelemetry with Zipkin/Tempo export

### Common Development Tasks
- **Add new endpoint**: Implement in respective service controller
- **Database changes**: Create Liquibase changelog files
- **Inter-service communication**: Use Feign clients with circuit breaker patterns
- **Configuration changes**: Update centralized config files and refresh via Spring Cloud Bus
- **New service**: Follow existing service structure with parent POM inheritance

## Important Notes

### Security Configuration
- OAuth2 Resource Server configuration in each service
- Keycloak realm configuration in `k8s/docker-compose/*/realm-config/`
- JWT token validation across all services

### Performance Considerations
- Caching implemented with Redis and Redisson
- Database connection pooling configured
- Async processing with `@Async` annotations
- Circuit breakers prevent cascading failures

### Environment Profiles
- `local`: Development with devtools, uses H2/PostgreSQL
- `prod`: Production profile with Kubernetes service discovery
- Additional profiles: `dev`, `qa` with environment-specific configurations