# fees-engine-core

A high-performance, stateless fee calculation engine designed for modern FinTech ecosystems. Optimized for Java 21 Virtual Threads (Project Loom) and Generational ZGC to handle massive transaction throughput with sub-millisecond latency.

---

## 🚀 Key Features

- **Loom-Native**: Utilizes a Virtual Thread-per-task model to eliminate I/O blocking bottlenecks.
- **Precision Math**: Implements Banker's Rounding (HALF_EVEN) and the Money Value Object pattern for financial accuracy.
- **Sealed Strategy Engine**: Uses Java 21 Sealed Interfaces and Pattern Matching for type-safe, high-speed calculation strategies.
- **RFC 7807 Support**: Standardized error handling using Problem Details for HTTP APIs.
- **Observability**: Integrated with Spring Boot Actuator for real-time monitoring of Virtual Thread counts and GC performance.

---

## 🛠 Tech Stack

- **Runtime**: Java 21 (OpenJDK / Temurin)
- **Framework**: Spring Boot 3.4.x
- **Database**: Designed for PostgreSQL 18+ (JSONB Rule Storage support)
- **Concurrency**: Project Loom (Virtual Threads)
- **Garbage Collector**: Generational ZGC

---

## 🚦 Quick Start

### Prerequisites

- JDK 21
- Maven 3.9+

### Running the Application

```bash
mvn clean install
mvn spring-boot:run
```

The server will start on:
```
http://localhost:8084
```

## 📡 Sample API Request
```
curl -X POST http://localhost:8084/v1/fees/calculate \
-H "Content-Type: application/json" \
-d '{
    "transactionId": "tx-2026-abc-123",
    "amount": 100.00,
    "currency": "USD",
    "merchantId": "merchant-77",
    "transactionCategory": "RETAIL"
}'
```

## 🧪 Testing
The project maintains a 100% logic-coverage suite, focusing on:

1. Financial integrity (Gross = Net + Fee)
2. Boundary conditions
3. Edge-case validation
### Run tests using:
    ```
    bash
    mvn test
    ```

## Performance Tuning
java -XX:+UseZGC -XX:+ZGenerational -XX:MaxRAMPercentage=75.0 -jar app.jar

## License
This project is licensed under the MIT License - see the LICENSE file for details.