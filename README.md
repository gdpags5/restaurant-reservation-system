# 🍽️ Restaurant Reservation System Backend

This is a Spring Boot–based backend service for managing restaurant reservations. It provides RESTful APIs for creating, updating, canceling, and retrieving reservations, with support for customer details, notification preferences, and reservation statuses. The system is designed for clarity, maintainability, and real-world reliability, featuring robust DTO validation and OpenAPI documentation.

---

## ⚙️ Installation Guidelines

### ✅ Prerequisites

Ensure the following tools are installed on your machine:

| Tool      | Version Required | Installation Guide |
|-----------|------------------|--------------------|
| **JDK**   | 17               | [Download JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) |
| **Maven** | 3.x              | [Install Maven](https://maven.apache.org/install.html) |
| **Postman** | Latest         | [Download Postman](https://www.postman.com/downloads/) |

> After installation, verify versions using:
> ```bash
> java -version
> mvn -version
> ```

---

## 🚀 How to Run the Program

1. Clone the repository:
   ```bash
   git clone https://github.com/gdpags5/restaurant-reservation-system.git
   cd restaurant-reservation-system
2. Build the project:
   ```bash
   mvn clean install
3. Run the application:
   ```bash
   mvn spring-boot:run
4. The backend will start
   `http://localhost:8080`

---

## 🧪 How to Test Using Postman
1. Open Postman and create a new request.
2. Set the request type to `POST`, `GET`, `PUT` or as needed.
3. Use the following sample endpoints:
   | Action | Method | Endpoint |
   |--------|--------|----------|
   | Create reservation | POST | `/reservations` |
   | Cancel reservation | POST | `/reservations/{id}/cancel` |
   | Update reservation | PUT  | `/reservations/{id}` |
   | Get by customer ID | GET  | `/reservations/customer/{id}` | 
4. Example `POST/reservations` payload:
   ```json
   {
   "customerDTO": {
       "id": 101,
       "name": "Juan dela Cruz",
       "email": "juandelacruz@email.com",
       "mobileNumber": "09171234567"
   },
   "numberOfGuests": 5,
   "reservationDate": "2025-11-08",
   "reservationTime": "18:30",
   "status": "PENDING",
   "methodOfNotification": "SMS"
  }
