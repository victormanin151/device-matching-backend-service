# Device Matching Backend Service

## Overview

Device Matching Backend Service is a Spring Boot REST API that identifies a device based on the client's **User-Agent** string. The application parses the operating system and browser information, searches for an existing matching device in Aerospike, and either increments its hit counter or creates a new device record if no match exists.

The project demonstrates REST API development, Spring Boot, Aerospike integration, exception handling, unit testing with JUnit and Mockito, and clean service-layer architecture.

---

# Features

* Parse User-Agent strings using YAUAA
* Match devices by:

  * Operating System Name
  * Operating System Version
  * Browser Name
  * Browser Version
* Create a new device if no match exists
* Increment hit count for existing devices
* Retrieve a device by ID
* Search devices by operating system
* Delete a single device
* Delete multiple devices
* Input validation
* Global exception handling with `@RestControllerAdvice`
* Service layer unit tests

---

# Technologies

* Java 21+
* Spring Boot
* Spring Data Aerospike
* Aerospike Database
* Maven
* Docker
* JUnit 5
* Mockito
* YAUAA (Yet Another UserAgent Analyzer)

---

# Project Structure

```text
src
├── main
│   ├── controller
│   ├── dto
│   ├── exception
│   ├── model
│   ├── parser
│   ├── repository
│   └── service
│
└── test
    ├── parser
    └── service
```

---

# Running the Project

## 1. Start Aerospike

Run Aerospike using Docker:

```bash
docker run -d \
  --name aerospike \
  --restart unless-stopped \
  --ulimit nofile=100000:100000 \
  -p 3000:3000 \
  aerospike/aerospike-server:latest
```

Verify the container is running:

```bash
docker ps
```

---

## 2. Configure the Application

Example `application.properties`

```properties
spring.aerospike.hosts=localhost:3000
spring.aerospike.namespace=test
```

---

## 3. Run the Application

```bash
mvn spring-boot:run
```

or

```bash
./mvnw spring-boot:run
```

---

# REST API

## Match or Create Device

**POST**

```
/devices/match
```

Header:

```
User-Agent
```

Example Response

```json
{
  "deviceId": "device-123",
  "hitCount": 1,
  "osName": "Windows NT",
  "osVersion": "10.0",
  "browserName": "Chrome",
  "browserVersion": "120"
}
```

---

## Get Device by ID

**GET**

```
/devices/{id}
```

---

## Search Devices by Operating System

**GET**

```
/devices?osName=Windows NT
```

Returns all matching devices.

---

## Delete Device

**DELETE**

```
/devices/{id}
```

Deletes a single device.

---

## Delete Multiple Devices

**DELETE**

```
/devices
```

Request Body

```json
[
  "device-1",
  "device-2",
  "device-3"
]
```

---

# Validation Rules

The application validates requests before interacting with the database.

Examples include:

* Device ID list cannot be null.
* Device ID list cannot be empty.
* Duplicate device IDs are not allowed.
* All requested IDs must exist before bulk deletion.
* Operating system name cannot be null or empty.

---

# Error Handling

The application uses a global exception handler (`@RestControllerAdvice`) to return meaningful HTTP responses.

| Situation               |                   HTTP Status |
| ----------------------- | ----------------------------: |
| Device not found        |             **404 Not Found** |
| Invalid request data    |           **400 Bad Request** |
| Unexpected server error | **500 Internal Server Error** |

Example error response:

```json
{
  "timestamp": "2026-08-03T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No device with that ID"
}
```

---

# Testing

The project contains unit tests for the service layer using **JUnit 5** and **Mockito**.

The test suite covers:

* Matching an existing device
* Creating a new device
* Finding a device by ID
* Searching by operating system
* Deleting a single device
* Bulk deletion
* Input validation
* Exception handling

Run all tests:

```bash
mvn test
```

---

# Design Decisions

* DTOs are returned instead of exposing entity classes directly.
* Constructor injection is used throughout the application.
* Business logic is isolated in the service layer.
* User-Agent parsing is implemented in a dedicated parser component.
* Validation is performed before repository interaction whenever possible.
* Bulk deletion follows an all-or-nothing approach.
* Custom exceptions and a global exception handler provide consistent HTTP responses.

---

# Future Improvements

* Controller/component tests using MockMvc

---

# Author

Victor Manin
