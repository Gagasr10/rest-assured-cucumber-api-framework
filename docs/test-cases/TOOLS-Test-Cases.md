TOOLS API – Test Cases

This document contains manual test cases for validating the Tools module of the Simple Tool Rental API. The goal is to verify functionality, input validation, filtering logic, error handling, and response contract consistency for the /tools endpoints.

Scope & Assumptions

Endpoints in scope:

GET /tools – Retrieve list of tools

GET /tools/:toolId – Retrieve a single tool by ID

Assumptions:

API is reachable and responsive.

No authentication is required for /tools endpoints.

Query parameters are optional unless specified.

Valid tool categories include:
ladders, plumbing, power-tools, trailers, electric-generators, lawn-care

The results parameter must accept values from 1 to 20 inclusive.

If unspecified, the default number of results is 20.

Test Cases
TC-TOOLS-001 – Get all tools (default)

Type: Positive (Smoke)
Priority: High
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools

Steps:

Send GET request to /tools

Expected Result:

Response status code is 200 OK

Response body is a JSON array

Array size is up to 20 items

Tags: @tools @smoke

TC-TOOLS-002 – Filter tools by valid category

Type: Positive
Priority: High
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools?category={category}

Test Data: ladders, plumbing, power-tools, trailers, electric-generators, lawn-care

Steps:

Send GET request using each valid category

Expected Result:

Status code is 200

Response is a JSON array

If category is included in response model, each returned tool matches the requested category

Tags: @tools

TC-TOOLS-003 – Filter tools by availability = true

Type: Positive
Priority: Medium
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools?available=true

Steps:

Send GET request with parameter available=true

Expected Result:

Status code is 200

Response is a JSON array

If availability field is present, returned items have availability=true

Tags: @tools

TC-TOOLS-004 – Filter tools by availability = false

Type: Positive
Priority: Medium
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools?available=false

Steps:

Send GET request with available=false

Expected Result:

Status code is 200

Response is a JSON array

If availability field is present, returned items have availability=false

Tags: @tools

TC-TOOLS-005 – Limit results using results query parameter

Type: Positive
Priority: Medium
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools?results=5

Steps:

Send GET request with results=5

Expected Result:

Status code is 200

Response is a JSON array

Array length is less than or equal to 5

Tags: @tools

TC-TOOLS-006 – Combined filter parameters

Type: Positive
Priority: Medium
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools?category=power-tools&available=true&results=5

Steps:

Send GET request with all three filters

Expected Result:

Status code is 200

Response body is a JSON array

Array size is <= 5

Values reflect valid filter combination

Tags: @tools

TC-TOOLS-007 – Invalid category input

Type: Negative
Priority: High
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools?category=invalid-category

Steps:

Send GET request using invalid category

Expected Result:

Status code is 400 Bad Request

Error message returned (if implemented)

Tags: @tools @negative

TC-TOOLS-008 – Invalid results parameter

Type: Negative
Priority: Medium
Preconditions:

API is available

Request:

Method: GET

Endpoints:

/tools?results=0

/tools?results=21

/tools?results=-5

Steps:

Send requests with each invalid value

Expected Result:

Status code is 400 Bad Request

Tags: @tools @negative

TC-TOOLS-009 – Get single tool by valid ID

Type: Positive
Priority: High
Preconditions:

Known valid toolId (example: 1)

Request:

Method: GET

Endpoint: /tools/1

Steps:

Send request to /tools/1

Expected Result:

Status code is 200 OK

Content-Type is JSON

Valid tool details are returned

Tags: @tools

TC-TOOLS-010 – JSON contract validation for tools list

Type: Contract
Priority: Medium
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools

Steps:

Validate schema against schemas/tools_list.json

Expected Result:

Status code is 200

Response body matches JSON schema

Tags: @tools @contract

TC-TOOLS-011 – JSON contract validation for single tool

Type: Contract
Priority: Medium
Preconditions:

Known valid toolId (example: 1)

Request:

Method: GET

Endpoint: /tools/1

Steps:

Validate response against schemas/tool_object.json

Expected Result:

Status code is 200

Response matches single tool schema

Tags: @tools @contract

TC-TOOLS-012 – Handle non-existent tool ID

Type: Negative
Priority: High
Preconditions:

API is available

Request:

Method: GET

Endpoint: /tools/999999

Steps:

Send GET request with large non-existent ID

Expected Result:

Status code is 404 Not Found

Tags: @tools @negative

TC-TOOLS-013 – Retrieve tool user manual (optional)

Type: Positive
Priority: Low
Preconditions:

Valid toolId

Request:

Method: GET

Endpoint: /tools/1?user-manual=true

Steps:

Send GET request for user manual

Expected Result:

Status code is 200

Response may return binary data or PDF-type content if available

Tags: @tools

JSON Schema References

tools list schema file: schemas/tools_list.json

single tool schema file: schemas/tool_object.json

Traceability to Automation (BDD)

Feature file: src/test/resources/features/tools.feature

Step definitions: src/test/java/com/example/api/stepdefinitions/CommonSteps.java

Tags used: @tools, @smoke, @negative, @contract

Execution Commands

Run all Tools tests:

mvn test -Denv=dev -Dcucumber.filter.tags="@tools"


Run only Negative tests:

mvn test -Denv=dev -Dcucumber.filter.tags="@tools and @negative"


Run only Contract tests:

mvn test -Denv=dev -Dcucumber.filter.tags="@tools and @contract"