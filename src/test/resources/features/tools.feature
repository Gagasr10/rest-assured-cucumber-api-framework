@tools
Feature: Tools API
  The Tools endpoints allow browsing and filtering of inventory.

  # -------------------------
  # Positive / Functional
  # -------------------------

  @smoke @regression
  Scenario: Get all tools
    When I send GET request to "/tools"
    Then the response status code should be 200
    And the content type should contain "application/json"
    And the response body should be a JSON array
    And the array size should be at most 20

  @regression
  Scenario Outline: Filter tools by valid category
    When I send GET request to "/tools?category=<category>"
    Then the response status code should be 200
    And the response body should be a JSON array
    And each item category should equal "<category>" when present

    Examples:
      | category            |
      | ladders             |
      | plumbing            |
      | power-tools         |
      | trailers            |
      | electric-generators |
      | lawn-care           |

  @regression
  Scenario: Filter tools by availability = true
    When I send GET request to "/tools?available=true"
    Then the response status code should be 200
    And the response body should be a JSON array
    And each item availability should be "true" when present

  @regression
  Scenario: Filter tools by availability = false
    When I send GET request to "/tools?available=false"
    Then the response status code should be 200
    And the response body should be a JSON array
    And each item availability should be "false" when present

  @regression
  Scenario Outline: Limit results to N within [1..20]
    When I send GET request to "/tools?results=<n>"
    Then the response status code should be 200
    And the response body should be a JSON array
    And the array size should be at most <n>

    Examples:
      | n  |
      | 1  |
      | 5  |
      | 20 |

  @regression
  Scenario: Combined filters (category + available + results)
    When I send GET request to "/tools?category=power-tools&available=true&results=5"
    Then the response status code should be 200
    And the response body should be a JSON array
    And the array size should be at most 5
    And each item category should equal "power-tools" when present
    And each item availability should be "true" when present

  # -------------------------
  # Negative
  # -------------------------

  @negative
  Scenario: Invalid category returns 400
    When I send GET request to "/tools?category=invalid-category"
    Then the response status code should be 400

 # Tolerated value: server returns 200 but caps/ignores to <=20
@regression
Scenario: results=0 is tolerated (server returns 200)
  When I send GET request to "/tools?results=0"
  Then the response status code should be 200
  And the response body should be a JSON array
  And the array size should be at most 20

# Truly invalid values: server returns 400
@negative
Scenario Outline: results out of range returns 400
  When I send GET request to "/tools?results=<bad>"
  Then the response status code should be 400

  Examples:
    | bad |
    | 21  |
    | -1  |



  @negative
  Scenario: Non-existent tool id returns 404
    When I send GET request to "/tools/99999999"
    Then the response status code should be 404

  # -------------------------
  # Contract (JSON Schema)
  # -------------------------

  @contract
  Scenario: Tools list matches schema
    When I send GET request to "/tools"
    Then the response status code should be 200
    And the response should match schema "schemas/tools_list.json"

  @contract
  Scenario: Single tool matches schema
    Given I pick a valid tool id from the tools list
    When I send GET request to that tool
    Then the response status code should be 200
    And the response should match schema "schemas/tool_object.json"
