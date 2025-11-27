# REST Assured + Cucumber API Automation Framework

![CI](https://github.com/Gagasr10/rest-assured-cucumber-api-framework/actions/workflows/ci.yml/badge.svg)

## Project Overview
This is a scalable API test automation framework built with **Java**, **Cucumber (BDD)** and **REST Assured**, designed for testing the **Simple Tool Rental API**.

It follows clean separation of concerns, reusable API clients, typed request/response models, environment configuration, JSON schema validation, full CI integration, and rich Allure reporting — ideal for a professional QA Automation portfolio.

---

## API Under Test
Target API: https://simple-tool-rental-api.click

Main endpoints:
- `GET /status` — API health check  
- `GET /tools`, `GET /tools/:toolId` — tools inventory  
- `POST /api-clients` — client registration (access token)  
- `GET /orders` — list all orders *(auth required)*  
- `GET /orders/:orderId` — fetch order *(auth required)*  
- `POST /orders` — create order *(auth required)*  
- `PATCH /orders/:orderId` — update customerName/status *(auth required)*  
- `DELETE /orders/:orderId` — delete order *(auth required)*  

---

## Technology Stack
- Java 17  
- Cucumber 7 + TestNG  
- REST Assured  
- Maven  
- AssertJ  
- JSON Schema Validator  
- Allure Reports (Cucumber 7 adapter)  
- GitHub Actions (CI)  

---

## Project Structure

```text
src/
  main/java/com/example/api/
    config/          → environment loader
    specs/           → shared request specs
    utils/           → helpers (token, tools lookup, context)
    models/
      errors/        → error DTOs
      orders/        → order request/response DTOs
    support/         → shared state (OrderState)
    clients/         → thin API clients (OrdersApi, etc.)

  test/java/com/example/api/
    runners/         → TestNG Cucumber runner
    stepdefinitions/ → Cucumber step definitions
    hooks/           → global cleanup hooks

  test/resources/
    features/        → Cucumber feature files
    schemas/         → JSON schemas
    config-dev.properties, config-*.properties


Setup & Running Tests
Prerequisites

Java 17+

Maven 3.9+

Internet connection (API is hosted online)

Run all tests
mvn clean test -Denv=dev

Run tests by tag
# Only orders tests
mvn clean test -Denv=dev -Dcucumber.filter.tags="@orders"

# Only smoke tests
mvn clean test -Denv=dev -Dcucumber.filter.tags="@smoke"

Allure Report

The project generates Allure results here:

target/allure-results

Local Allure report (requires Allure CLI)
allure serve target/allure-results

From GitHub Actions

Open the Actions tab

Select a CI run

Download the allure-results artifact

Run locally:

allure serve path/to/allure-results


Produces a full HTML report with:

scenarios

steps

timings

request/response logs (if enabled)

attachments

environment details

Test Strategy & Tags

The suite uses Cucumber tags to organize tests:

Tag	Meaning
@smoke	Basic health checks
@regression	Full functional coverage
@orders	Tests for Orders API
@contract	JSON schema validation
@negative	Invalid payloads, auth errors, 4xx responses

Included test types:

Happy-path create/fetch/delete flow

Contract validation (schemas)

Negative scenarios (401, 404, invalid payloads)

Behavior for empty/minimal values

Status and customerName update scenarios

CI — GitHub Actions

Workflow file:

.github/workflows/ci.yml


Pipeline performs:

Checkout repository

Set up Java 17

Cache Maven dependencies

Run:

mvn -B -Denv=dev clean test


Upload:

Cucumber HTML and JSON reports

Allure results

The CI badge at the top of this README shows the current pipeline status.

Retry & Stability

The framework includes a retry helper for REST calls.
Useful for:

Unstable /tools endpoint

Transient 502/503 server issues

Increasing test stability on CI

Author

Dragan Stojilkovic — QA Automation Engineer

Specialized in:

Java

REST Assured

Cucumber (BDD)

TestNG

Allure reporting

CI/CD with GitHub Actions

GitHub: https://github.com/Gagasr10

LinkedIn: https://www.linkedin.com/in/dragan-stojilkovic-35aa8426a/

Email: dragan.stojilkovic85@gmail.com