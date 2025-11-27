# REST Assured + Cucumber API Automation Framework

![CI](https://github.com/Gagasr10/rest-assured-cucumber-api-framework/actions/workflows/ci.yml/badge.svg)

## Project Overview
This is a scalable API test automation framework built with **Java**, **Cucumber (BDD)** and **REST Assured**, designed for testing the **Simple Tool Rental API**.  
It follows clean separation of concerns, readable BDD-style scenarios, environment configuration, logging and CI integration — suitable for a professional portfolio or hiring process.

---

## API Under Test
Target API: [Simple Tool Rental API](https://simple-tool-rental-api.click)

Main endpoints:

- `GET /status` – API health check
- `GET /tools` and `GET /tools/:toolId` – tools inventory
- `POST /api-clients` – register client and obtain access token
- `GET /orders` – list all orders (auth required)
- `GET /orders/:orderId` – fetch single order (auth required)
- `POST /orders` – create order (auth required)
- `PATCH /orders/:orderId` – update order (status or customerName) (auth required)
- `DELETE /orders/:orderId` – delete order (auth required)

---

## Technology Stack

- Java 17
- Cucumber 7 + TestNG runner
- REST Assured
- Maven
- JSON Schema Validator
- AssertJ
- Allure Reports (Cucumber 7 adapter)
- GitHub Actions (CI on `main` branch)

---

## Project Structure

```text
src/
  main/java/com/example/api/
    config/          -> environment loader (config-dev.properties, etc.)
    specs/           -> shared REST Assured request/response specs
    utils/           -> helpers (token manager, request utils)
    models/
      errors/        -> error response DTOs
      orders/        -> order request/response DTOs
    support/         -> shared state between steps (OrderState)
    clients/         -> thin API clients (OrdersApi, etc.)

  test/java/com/example/api/
    runners/         -> Cucumber TestNG runner (RunTest)
    stepdefinitions/ -> Cucumber step definitions
    hooks/           -> global hooks (cleanup etc.)

  test/resources/
    features/        -> Cucumber feature files (orders, status, tools)
    schemas/         -> JSON schemas for contract tests
    config-dev.properties, config-*.properties

