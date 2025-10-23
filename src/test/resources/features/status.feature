
@smoke @status
Feature: API Status
  As a user of the Tool Rental API
  I want to check the health endpoint
  So that I can verify the API is up

  Scenario: API returns UP status
    When I send GET request to "/status"
    Then the response status code should be 200
    And the response should indicate the API is "UP"
