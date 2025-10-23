# rest-assured-cucumber-api-framework

## 🧪 Project Overview
This is a scalable API test automation framework built with **Java**, **Cucumber (BDD)** and **REST Assured**, designed for testing the *Simple Tool Rental API*. It follows clean separation of concerns, readable BDD-style scenarios, environment configuration, logging and CI/CD integration — ideal for freelance showcase or hiring opportunities.

## 🚀 API Under Test
The framework targets the [Simple Tool Rental API](https://simple-tool-rental-api.click) which allows reserving tools and managing orders.

Key endpoints:
- `GET /status` – API health
- `GET /tools` and `GET /tools/:toolId` – Inventory
- `POST /api-clients` – Register client & obtain access token
- `GET /orders`, `GET /orders/:orderId`, `POST /orders`, `PATCH /orders/:orderId`, `DELETE /orders/:orderId` – Orders (auth required)

## 🧩 Technology Stack
- Java 17+
- Cucumber (BDD) + TestNG runner
- REST Assured
- Maven
- JSON Schema Validator
- Allure Reports
- GitHub Actions (CI)

## 🧬 Project Structure
```
src/
  main/java/com/example/api/
    config/         → env loader, configuration
    specs/          → request/response specs
    utils/          → helpers (token mgmt, json, context)
    models/         → POJOs (add as needed)
  test/java/com/example/api/
    runners/        → TestNG Cucumber runner
    stepdefinitions/→ step defs per feature
  test/resources/
    features/       → Cucumber feature files
    schemas/        → JSON schema files
    config-*.properties
```

## 🛠 Setup & Usage
1) Install Java 17+ and Maven 3.9+  
2) Set environment (default `dev`) and run:
```bash
mvn clean test -Denv=dev
```
3) Run by tags:
```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```
4) Allure report (requires Allure CLI):
```bash
allure serve target/allure-results
```

## 📝 Test Strategy
- **@smoke** — health & basic happy paths
- **@regression** — broader coverage
- **@contract** — schema validation
- **@negative** — invalid payloads, auth errors, 4xx

## 🔧 CI
GitHub Actions workflow runs tests on push/PR to `main` and uploads Allure results as artifacts. Badge can be added after the first successful run.

## 📖 License
MIT License

---

### 👨‍💻 Author
**Dragan S. – QA Automation Engineer**  
Passionate about designing scalable API test frameworks using Java, REST Assured, Cucumber (BDD) and CI/CD pipelines. Focused on quality engineering and clean automation design.  
🔗 [GitHub](https://github.com/Gagasr10) | 🔗 [LinkedIn](https://www.linkedin.com/in/dragan-stojilkovic-35aa8426a/)
