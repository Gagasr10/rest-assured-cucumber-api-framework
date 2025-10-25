@epic:Orders
@feature:Orders-CRUD
@owner:qa.team
@severity:normal
@regression
@orders
Feature: Orders management
  Authenticated clients should manage orders

  Background:
    # Acquire a valid token for scenarios that use authorized steps
    Given I have a valid access token

  @story:Create
  @severity:critical
  Scenario: Create a new order
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    And the order customer name should be "John Doe"

  @story:Read
  @severity:normal
  Scenario: Get all orders
    When I send an authorized GET request to "/orders"
    Then the response status code should be 200

  @contract
  @story:Read
  @severity:normal
  Scenario: Orders list matches schema
    When I send an authorized GET request to "/orders"
    Then the response status code should be 200
    And the response should match schema "schemas/orders_list.json"

  @story:Read
  @severity:normal
  Scenario: Orders list has required fields
    When I send an authorized GET request to "/orders"
    Then the response status code should be 200
    And each order item should have required fields

  @negative
  @story:Security
  @severity:minor
  Scenario: Unauthorized orders access returns 401
    When I send GET request to "/orders"
    Then the response status code should be 401
    And the response should match error schema

  @negative
  @story:Read
  @severity:minor
  Scenario: Get non-existent order returns 404
    When I send an authorized GET request to "/orders/does-not-exist"
    Then the response status code should be 404
    And the response should match error schema

  @negative
  @story:Create
  @severity:minor
  Scenario: Create order with invalid payload returns 400
    When I send an authorized POST request to "/orders" with body:
      """
      {"toolId": "not-a-number", "customerName": ""}
      """
    Then the response status code should be 400
    And the response should match error schema

  @contract
  @story:Create
  @severity:major
  Scenario: Created order matches schema
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    And the response should match schema "schemas/order_object.json"
    And the order customer name should be "John Doe"

  @negative
  @orders
  @story:Delete
  @severity:normal
  Scenario: Delete non-existent order returns 404
    When I send an authorized DELETE request to "/orders/99999999"
    Then the response status code should be 404
    And the response should match error schema

  @regression
  @orders
  @contract
  @story:Read
  @severity:normal
  Scenario: Create then fetch a single order
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    When I fetch that order
    Then the response status code should be 200
    And the response should match schema "schemas/order_object.json"

  @regression
  @orders
  @story:Delete
  @severity:critical
  Scenario: Create then delete an order
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    When I delete that order
    Then the response status code should be one of 200 or 204

  @negative
  @orders
  @story:Delete
  @severity:normal
  Scenario: Delete already deleted order returns 404
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    When I delete that order
    Then the response status code should be one of 200 or 204
    When I delete that order
    Then the response status code should be 404
    And the response should match error schema

  @regression
  @orders
  @negative
  @story:Security
  @severity:minor
  Scenario: Delete order without auth returns 401
    # This step intentionally sends NO auth header
    When I send DELETE request to "/orders/unauth-test"
    Then the response status code should be 401
    And the response should match error schema

  @regression
  @orders
  @story:Update
  @severity:major
  Scenario: Update an existing order customerName
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    When I update that order customer name to "Jane Doe"
    Then the response status code should be one of 200 or 204
    And the order customer name should be "Jane Doe"

  @negative
  @orders
  @story:Security
  @severity:minor
  Scenario: Update order without auth returns 401
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    # This step intentionally sends NO auth header
    When I send PATCH request to that order with body:
      """
      {
        "customerName": "NoAuth Change"
      }
      """
    Then the response status code should be 401
    And the response should match error schema

  @negative
  @orders
  @story:Update
  @severity:normal
  Scenario: Update order with empty customerName results in empty value
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    When I update that order customer name to ""
    Then the response status code should be one of 200 or 204
    When I fetch that order
    Then the response status code should be 200
    And the order customer name should be ""

  @negative
  @orders
  @story:Update
  @severity:normal
  Scenario: Update a non-existent order returns 404
    Given I have a valid access token
    When I send an authorized PATCH request to "/orders/does-not-exist" with body:
      """
      { "status": "approved" }
      """
    Then the response status code should be 404
    And the response should match error schema
