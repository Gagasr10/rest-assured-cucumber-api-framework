@auth
Feature: Register API client
  API client registration and token handling

  Scenario: Register a new API client (token is available)
    When I register a new API client
    Then the response status code should be one of 201 or 409
    And an access token should be returned

  Scenario: Duplicate registration returns 409
    When I register a new API client
    Then the response status code should be 409
