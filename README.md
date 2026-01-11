# Virtual Card Issuance Platform

---
## 📌 Overview

This project implements a **Virtual Card Issuance and Spending API** using **Spring Boot**, designed according to clean architecture principles and REST best practices.
The system allows users to:

* Create virtual cards
* Spend money from cards (without allowing overdraft)
* Top-up card balance
* View card details
* View transaction history

The implementation focuses on **correct business logic**, **clean layering**, **robust validations**, and **extensibility**, as required by the assignment.

---
## 🏗️ Architecture & Design

The application follows a **layered architecture**:

```
Controller → Service → Repository → Database
```

### Layer Responsibilities

* **Controller**

    * Handles HTTP requests & responses
    * Performs request validation
    * Delegates business logic to the service layer

* **Service**

    * Contains all business rules
    * Ensures balance never goes below zero
    * Manages transactional consistency

* **Repository**

    * Handles database interaction using Spring Data JPA

* **Entity**

    * Represents domain models (`Card`, `Transaction`)

* **DTOs**

    * Isolate API contracts from persistence models

This separation ensures **maintainability, testability, and scalability**.

---

## 🔧 Technology Stack

* Java 17
* Spring Boot
* Spring Data JPA
* H2 In-Memory Database
* Hibernate
* Jakarta Validation
* OpenAPI / Swagger
* SLF4J Logging

---

## 📚 API Endpoints

### 1️⃣ Create Card

**POST** `/cards`

```json
{
  "cardholderName": "Alice",
  "initialBalance": 100.00
}
```

✔️ Creates a new card
✔️ Initial balance is recorded as a **TOP_UP transaction**

**Response:** `201 Created`

---

### 2️⃣ Spend from Card

**POST** `/cards/{id}/spend`

```json
{
  "amount": 30.00
}
```

**Rules**

* ❌ Rejects transaction if amount > available balance
* ✅ Deducts amount otherwise

**Response:** `200 OK`

---

### 3️⃣ Top-Up Card

**POST** `/cards/{id}/topup`

```json
{
  "amount": 50.00
}
```

✔️ Increases card balance
✔️ Records a **TOP_UP transaction**

---

### 4️⃣ Get Card Details

**GET** `/cards/{id}`

Returns card details including current balance.

---

### 5️⃣ Get Transaction History

**GET** `/cards/{id}/transactions`

✔️ Returns all transactions sorted by newest first
✔️ Includes both **SPEND** and **TOP_UP**

---

## 📘 API Documentation (Swagger)

The project is integrated with **OpenAPI / Swagger** for interactive API documentation and testing.

Once the application is running, the Swagger UI can be accessed at:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger provides:

* Complete list of available endpoints
* Request and response schemas
* Ability to test APIs directly from the browser

This was used during development to validate API contracts and responses.

---

## 💼 Business Logic & Correctness

The following rules are strictly enforced:

* A card balance **never goes below zero**
* Spending beyond available balance throws `InsufficientBalanceException`
* Card existence is validated before any operation
* All monetary values use `BigDecimal` for precision

Transactions are recorded atomically to ensure consistency.

---

## 🗄️ Database Design

### Card Entity

* `id`
* `cardholderName`
* `balance`
* `createdAt`

### Transaction Entity

* `id`
* `card`
* `amount`
* `type` (SPEND / TOP_UP)
* `createdAt`

JPA annotations are used for ORM mapping and automatic schema generation.

---

## 🚨 Exception Handling & Validation

* Custom exceptions:

    * `CardNotFoundException`
    * `InsufficientBalanceException`
* Input validation using `@Valid` and Jakarta Bean Validation
* Clear and meaningful error responses
* Logging added at appropriate levels (`INFO`, `DEBUG`, `WARN`)

---

## 🧪 Testing Strategy

### Manual Test Plan

| Scenario             | Expected Result                   |
| -------------------- | --------------------------------- |
| Create card          | Card created with correct balance |
| Spend within balance | Balance deducted successfully     |
| Spend beyond balance | Request rejected                  |
| Top-up card          | Balance increased                 |
| Fetch invalid card   | `404 Not Found`                   |
| View transactions    | Correct order and type            |

### Automated Testing (Planned / Partial)

* Service and Controller layer unit tests using JUnit & Mockito
* Mock repositories to validate:

    * Spend logic
    * Balance validation
    * Exception scenarios

---

## 🔐 Transaction Management & Consistency

* `@Transactional` ensures:

    * Atomic balance update
    * Transaction history consistency
* Prevents partial updates in case of failures

---

## 📈 Optional Extensions Implemented

✔️ Transaction history
✔️ Top-up functionality

Future enhancements could include:

* Optimistic locking for concurrency
* Pagination for transactions
* Authentication & authorization
* Persistent database (PostgreSQL/MySQL)

---

## ⚖️ Trade-offs

* Used H2 for simplicity and fast setup
* Focused more on correctness and design than UI or security
* Limited automated tests due to time constraints

---

## 🚀 Improvements with More Time

* Add optimistic locking (`@Version`)
* Improve API error response standardization
* Add integration tests
* Implement rate limiting and authentication
* Introduce event-driven transaction logging

---

## 📝 Final Notes

This implementation prioritizes **business correctness**, **clean architecture**, and **explainability**, aligning closely with the evaluation criteria for the SDE-II role.

---