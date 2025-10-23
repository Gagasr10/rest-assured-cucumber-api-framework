
@regression @tools
Feature: Tools inventory
  Users should be able to browse tools

  Scenario: Get all tools
    When I send GET request to "/tools"
    Then the response status code should be 200

  Scenario Outline: Filter tools by category
    When I send GET request to "/tools?category=<category>"
    Then the response status code should be 200
    Examples:
      | category            |
      | ladders             |
      | plumbing            |
      | power-tools         |
      | trailers            |
      | electric-generators |
      | lawn-care           |

  @contract
  Scenario: Tools list matches schema
    When I send GET request to "/tools"
    Then the response status code should be 200
    And the response should match schema "schemas/tools_list.json"

  @contract
  Scenario: Single tool matches schema
    When I send GET request to "/tools/1"
    Then the response status code should be 200
    And the response should match schema "schemas/tool_object.json"

  @negative
  Scenario: Invalid category returns 400
    When I send GET request to "/tools?category=invalid-category"
    Then the response status code should be 400

  @negative
  Scenario: Non-existent tool returns 404
    When I send GET request to "/tools/99999999"
    Then the response status code should be 404
