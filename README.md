# REST Assured + Cucumber API Automation Framework

![CI](https://github.com/Gagasr10/rest-assured-cucumber-api-framework/actions/workflows/ci.yml/badge.svg)

A clean and scalable **API test automation framework** built with **Java**, **REST Assured**, **Cucumber (BDD)** and **TestNG**.  
Designed to demonstrate a professional automation setup with **environment configs**, **DTO models**, **test tagging strategy**, **JSON schema validation**, and **CI integration**.

---

## ✅ API Under Test

The framework tests the **Simple Tool Rental API**, which supports:
| Area | Endpoints |
|-------|-----------|
| API Health | `GET /status` |
| Tools | `GET /tools`, `GET /tools/{id}` |
| Auth | `POST /api-clients` |
| Orders (auth-required) | `GET/POST/PATCH/DELETE /orders` |

API Docs: https://simple-tool-rental-api.click

---

## ⚙️ Tech Stack
- Java 17
- REST Assured
- Cucumber (BDD)
- TestNG
- Maven
- AssertJ
- JSON Schema Validation
- Allure Reports
- GitHub Actions CI

---

## 📁 Project Structure

