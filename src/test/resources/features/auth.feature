
@smoke @auth
Feature: Register API client
  To interact with protected endpoints
  I need to register a client and get an access token

  Scenario: Register a new API client and receive token
    When I register a new API client
    Then the response status code should be 201
    And an access token should be returned

  @negative
  Scenario: Duplicate registration returns 409
    When I register a new API client with email "qa.duplicate@example.com"
    Then the response status code should be 201
    And an access token should be returned
    When I register a new API client with email "qa.duplicate@example.com"
    Then the response status code should be 409
