
@regression @orders
Feature: Orders management
  Authenticated clients should manage orders

  Background:
    Given I have a valid access token

  Scenario: Create a new order
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201

  Scenario: Get all orders
    When I send an authorized GET request to "/orders"
    Then the response status code should be 200

  @contract
  Scenario: Orders list matches schema
    When I send an authorized GET request to "/orders"
    Then the response status code should be 200
    And the response should match schema "schemas/orders_list.json"

  @negative
  Scenario: Unauthorized orders access returns 401
    When I send GET request to "/orders"
    Then the response status code should be 401

  @negative
  Scenario: Get non-existent order returns 404
    When I send an authorized GET request to "/orders/does-not-exist"
    Then the response status code should be 404

  @negative
  Scenario: Create order with invalid payload returns 400
    When I send an authorized POST request to "/orders" with body:
      """
      {"toolId": "not-a-number", "customerName": ""}
      """
    Then the response status code should be 400

  @contract
  Scenario: Created order matches schema
    When I create a new order with a valid tool id and customer name
    Then the response status code should be 201
    And the response should match schema "schemas/order_object.json"

  @negative
  Scenario: Delete non-existent order returns 404
    When I send an authorized DELETE request to "/orders/does-not-exist"
    Then the response status code should be 404
    
   @regression @orders @contract
	Scenario: Create then fetch a single order
  Given I have a valid access token
  When I create a new order with a valid tool id and customer name
  Then the response status code should be 201
  When I fetch that order
  Then the response status code should be 200
  And the response should match schema "schemas/order_object.json"
  
  @regression @orders
	Scenario: Create then delete an order
  Given I have a valid access token
  When I create a new order with a valid tool id and customer name
  Then the response status code should be 201
  When I delete that order
  Then the response status code should be one of 200 or 204
  
  @negative @orders
  Scenario: Delete already deleted order returns 404
  Given I have a valid access token
  When I create a new order with a valid tool id and customer name
  Then the response status code should be 201
  When I delete that order
  Then the response status code should be one of 200 or 204
  When I delete that order
  Then the response status code should be 404


@regression @orders @negative
Scenario: Delete order without auth returns 401
  When I send DELETE request to "/orders/unauth-test"
  Then the response status code should be 401



