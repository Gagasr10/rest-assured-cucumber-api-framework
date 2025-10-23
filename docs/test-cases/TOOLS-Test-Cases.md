# TOOLS — Manual Test Cases (GET /tools, GET /tools/:toolId)

This document contains the manual test scenarios for validating the `/tools` endpoints of the Simple Tool Rental API. The goal is to verify correctness, filtering behavior, negative test handling, and JSON contract compliance.

**Module:** Inventory (Tools)  
**Base URL:** https://simple-tool-rental-api.click  
**Author:** Dragan S. – QA Automation Engineer  
**Automation Coverage:** Yes (Cucumber + REST Assured + TestNG)  
**Tags Used:** @tools, @smoke, @negative, @contract, @regression

---

## Scope & Assumptions

Endpoints in scope:
- `GET /tools`
- `GET /tools/:toolId`

Assumptions:
- No authentication is required for the `/tools` endpoints.
- Valid tool categories as per API documentation:
  `ladders`, `plumbing`, `power-tools`, `trailers`, `electric-generators`, `lawn-care`
- `results` query parameter must be within `[1..20]`
- Optional filters supported:
  - `available=true|false`
  - `user-manual=true` (optional, may return binary PDF)

--------------------------------------------------------------------------------
## TC-TOOLS-001 – Get all tools (default)

**Type:** Positive (Smoke)  
**Priority:** High  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools`

**Steps:**  
1. Send GET request to `/tools`

**Expected Result:**  
- Status code is `200 OK`  
- Response body is a JSON array  
- Default number of tools returned is up to 20 items  

**Tags:** @tools @smoke

--------------------------------------------------------------------------------
## TC-TOOLS-002 – Filter tools by valid category

**Type:** Positive  
**Priority:** High  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools?category={cat}`  
- Test Data: `ladders`, `plumbing`, `power-tools`, `trailers`, `electric-generators`, `lawn-care`

**Steps:**  
1. Send GET request using each valid category as query parameter  

**Expected Result:**  
- Status code is `200 OK`  
- Response body is a JSON array  
- All returned tools (if category field present) match selected category  

**Tags:** @tools

--------------------------------------------------------------------------------
## TC-TOOLS-003 – Filter tools by availability = true

**Type:** Positive  
**Priority:** Medium  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools?available=true`

**Steps:**  
1. Send GET request with `available=true`

**Expected Result:**  
- Status code is `200 OK`  
- Response body is a JSON array  
- Tools are available (if availability attribute is present)

**Tags:** @tools

--------------------------------------------------------------------------------
## TC-TOOLS-004 – Filter tools by availability = false

**Type:** Positive  
**Priority:** Medium  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools?available=false`

**Steps:**  
1. Send GET request with `available=false`

**Expected Result:**  
- Status code is `200 OK`  
- Response body is a JSON array  
- Returned tools are not available (if availability attribute is present)

**Tags:** @tools

--------------------------------------------------------------------------------
## TC-TOOLS-005 – Limit results with results=N

**Type:** Positive  
**Priority:** Medium  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools?results=5`

**Steps:**  
1. Send GET request with `results=5`

**Expected Result:**  
- Status code is `200 OK`  
- Response body is a JSON array  
- Array size is less than or equal to 5

**Tags:** @tools

--------------------------------------------------------------------------------
## TC-TOOLS-006 – Combined filter parameters

**Type:** Positive  
**Priority:** Medium  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools?category=power-tools&available=true&results=5`

**Steps:**  
1. Send GET request with all three filters

**Expected Result:**  
- Status code is `200 OK`  
- Response body is a JSON array  
- Array size is <= 5  
- Values return valid filter combination

**Tags:** @tools
--------------------------------------------------------------------------------
## TC-TOOLS-007 – Invalid category value

**Type:** Negative  
**Priority:** High  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools?category=invalid-category`

**Steps:**  
1. Send GET request using an invalid category value

**Expected Result:**  
- Status code is `400 Bad Request`  
- Response contains error details (if provided)

**Tags:** @tools @negative

--------------------------------------------------------------------------------
## TC-TOOLS-008 – Invalid results parameter returns 400

**Type:** Negative  
**Priority:** Medium  
**Preconditions:** API is reachable  

**Request:**  
Test the following invalid `results` values:
- `/tools?results=0`
- `/tools?results=21`
- `/tools?results=-1`

**Steps:**  
1. Send GET request with each invalid results value

**Expected Result:**  
- Status code is `400 Bad Request` for each request

**Tags:** @tools @negative

--------------------------------------------------------------------------------
## TC-TOOLS-009 – Get single tool by valid ID

**Type:** Positive  
**Priority:** High  
**Preconditions:** A valid `toolId` is known (e.g. 1)  

**Request:**  
- Method: GET  
- Endpoint: `/tools/1`

**Steps:**  
1. Send GET request to `/tools/1`

**Expected Result:**  
- Status code is `200 OK`  
- Response is a JSON object  
- Response contains details for the tool

**Tags:** @tools

--------------------------------------------------------------------------------
## TC-TOOLS-010 – JSON schema validation for tools list

**Type:** Contract  
**Priority:** Medium  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools`

**Steps:**  
1. Send GET request to `/tools`
2. Validate response matches `schemas/tools_list.json`

**Expected Result:**  
- Status code is `200 OK`  
- Response structure matches JSON schema

**Tags:** @tools @contract

--------------------------------------------------------------------------------
## TC-TOOLS-011 – JSON schema validation for single tool

**Type:** Contract  
**Priority:** Medium  
**Preconditions:** A valid `toolId` is known (e.g. 1)  

**Request:**  
- Method: GET  
- Endpoint: `/tools/1`

**Steps:**  
1. Send GET request to `/tools/1`
2. Validate response matches `schemas/tool_object.json`

**Expected Result:**  
- Status code is `200 OK`  
- Response matches JSON schema

**Tags:** @tools @contract
--------------------------------------------------------------------------------
## TC-TOOLS-012 – Non-existing tool ID returns 404

**Type:** Negative  
**Priority:** High  
**Preconditions:** API is reachable  

**Request:**  
- Method: GET  
- Endpoint: `/tools/99999999`

**Steps:**  
1. Send GET request using a large or invalid `toolId`

**Expected Result:**  
- Status code is `404 Not Found`

**Tags:** @tools @negative

--------------------------------------------------------------------------------
## TC-TOOLS-013 – Optional: Request user manual (PDF)

**Type:** Positive  
**Priority:** Low  
**Preconditions:** A valid `toolId` is known (e.g. 1)  

**Request:**  
- Method: GET  
- Endpoint: `/tools/1?user-manual=true`

**Steps:**  
1. Send GET request with query parameter `user-manual=true`

**Expected Result:**  
- Status code is `200 OK`  
- Response `Content-Type` includes `application/pdf`

**Tags:** @tools

--------------------------------------------------------------------------------

## JSON Contract Validation

| Schema File | Description |
|-------------|-------------|
| `schemas/tools_list.json` | Defines structure for tools array response |
| `schemas/tool_object.json` | Defines structure for a single tool object |

Schemas are currently relaxed to maintain CI stability. They can be enhanced later by gradually enforcing required fields and stricter types.

--------------------------------------------------------------------------------

## Traceability to Automation (BDD)

| Component                                | Location |
|------------------------------------------|----------|
| Feature file                             | `src/test/resources/features/tools.feature` |
| Step definitions                         | `src/test/java/com/example/api/stepdefinitions/CommonSteps.java` |
| Runner                                   | `src/test/java/com/example/api/runners/RunTest.java` |
| JSON Schemas                             | `src/test/resources/schemas/` |

--------------------------------------------------------------------------------

## Execution Hints

Run **all Tools tests**:

