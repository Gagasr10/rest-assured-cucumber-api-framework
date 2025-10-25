package com.example.api.stepdefinitions;

import com.example.api.specs.Specs;
import com.example.api.utils.TokenManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.cucumber.java.After;
import com.example.api.utils.RequestUtils;
import com.example.api.support.OrderState;
import com.example.api.clients.OrdersApi;
import com.example.api.utils.RequestUtils;




import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.api.support.OrderState;


public class CommonSteps {

    private Response response;
    private String token;
    private Integer toolId;
    private String createdOrderId;
 //   private String lastCreatedOrderId;
    


    // -------------------------
    // Generic REST steps
    // -------------------------
    
    private void ensureToolId() {
        if (this.toolId != null) return;

        Response r = given()
                .spec(Specs.request())
                .when()
                .get("/tools")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var list = r.jsonPath().getList("$");
        for (Object o : list) {
            if (o instanceof java.util.Map<?, ?> m && m.get("id") != null) {
                this.toolId = Integer.valueOf(m.get("id").toString());
                return;
            }
        }
        throw new IllegalStateException("No tool with an 'id' found in /tools response: " + r.asString());
    }

    


    @When("I send GET request to {string}")
    public void i_send_get_request_to(String path) {
        response = given()
                .spec(Specs.request())
                .when()
                .get(path);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer code) {
        assertThat(response.getStatusCode()).isEqualTo(code);
    }

    @Then("the response should indicate the API is {string}")
    public void the_response_should_indicate_api_is(String expected) {
        String body = response.asString();
        assertThat(body).contains(expected);
    }

    @Then("the content type should contain {string}")
    public void the_content_type_should_contain(String expected) {
        assertThat(response.getContentType()).containsIgnoringCase(expected);
    }

    @Then("the response body should be a JSON array")
    public void the_response_body_should_be_a_json_array() {
        List<?> list = response.jsonPath().getList("$");
        assertThat(list).as("Body should be a JSON array").isNotNull();
    }

    @Then("the array size should be at most {int}")
    public void the_array_size_should_be_at_most(Integer n) {
        List<?> list = response.jsonPath().getList("$");
        assertThat(list.size()).isLessThanOrEqualTo(n);
    }

    @Then("each item category should equal {string} when present")
    public void each_item_category_should_equal_when_present(String expected) {
        List<?> items = response.jsonPath().getList("$");
        for (Object o : items) {
            if (o instanceof Map<?, ?> m) {
                Object cat = m.get("category");
                if (cat != null) {
                    assertThat(cat.toString()).isEqualTo(expected);
                }
            }
        }
    }

    @Then("each item availability should be {string} when present")
    public void each_item_availability_should_be_when_present(String expectedStr) {
        boolean expected = Boolean.parseBoolean(expectedStr);
        List<?> items = response.jsonPath().getList("$");
        for (Object o : items) {
            if (o instanceof Map<?, ?> m) {
                // neke verzije koriste "available", neke "availability" – proveri oba ključa
                Object a = m.containsKey("available") ? m.get("available") : m.get("availability");
                if (a != null) {
                    if (a instanceof Boolean b) {
                        assertThat(b).isEqualTo(expected);
                    } else {
                        assertThat(Boolean.parseBoolean(a.toString())).isEqualTo(expected);
                    }
                }
            }
        }
    }

    @Then("the response should match schema {string}")
    public void the_response_should_match_schema(String schemaPath) {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(schemaPath));
    }

    // -------------------------
    // Auth / Orders steps
    // -------------------------

    @When("I register a new API client")
    public void i_register_a_new_api_client() {
        token = TokenManager.getOrCreateToken();
        response = given()
                .spec(Specs.request())
                .body("{\"clientName\":\"client\",\"clientEmail\":\"dup@example.com\"}")
                .when()
                .post("/api-clients");
    }

    @Then("an access token should be returned")
    public void an_access_token_should_be_returned() {
        assertThat(token).isNotBlank();
    }

    @Given("I have a valid access token")
    public void i_have_a_valid_access_token() {
        token = TokenManager.getOrCreateToken();
        assertThat(token).isNotBlank();
    }

    @When("I send an authorized GET request to {string}")
    public void i_send_an_authorized_get_request_to(String path) {
        if ("/orders".equals(path)) {
            response = OrdersApi.getAll();
        } else if (path.startsWith("/orders/")) {
            String id = path.substring("/orders/".length());
            response = OrdersApi.get(id);
        } else {
            // Fallback for other authorized GETs if any appear later
            response = given()
                    .spec(Specs.request())
                    .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                    .when()
                    .get(path);
        }
    }
    
    @When("I create a new order with a valid tool id and customer name")
    public void i_create_a_new_order_with_valid_tool_id_and_customer_name() {
        int toolId = RequestUtils.pickFirstValidToolId();
        response = OrdersApi.create(toolId, "John Doe");

        // Remember orderId in shared state (used by fetch/delete/patch steps and Hooks cleanup)
        String orderId = response.jsonPath().getString("orderId");
        assertThat(orderId)
                .as("orderId must be returned on successful creation; body: %s", response.asString())
                .isNotBlank();
        OrderState.setLastCreatedOrderId(orderId);
    }


    


    @When("I send an authorized POST request to {string} with body:")
    public void i_send_an_authorized_post_request_to_with_body(String path, String docString) {
        response = given()
                .spec(RequestUtils.authSpec())
                .body(docString)
                .when()
                .post(path);
    }

    @When("I send an authorized DELETE request to {string}")
    public void i_send_an_authorized_delete_request_to(String path) {
        response = given()
                .spec(RequestUtils.authSpec())
                .when()
                .delete(path);
    }

    @When("I register a new API client with email {string}")
    public void i_register_a_new_api_client_with_email(String email) {
        String payload = String.format("{\"clientName\":\"framework-client\",\"clientEmail\":\"%s\"}", email);
        response = given()
                .spec(Specs.request())
                .body(payload)
                .when()
                .post("/api-clients");
    }
    
    @Given("I pick a valid tool id from the tools list")
    public void i_pick_a_valid_tool_id_from_the_tools_list() {
        Response r = given()
                .spec(Specs.request())
                .when()
                .get("/tools")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var list = r.jsonPath().getList("$");
        for (Object o : list) {
            if (o instanceof java.util.Map<?, ?> m && m.get("id") != null) {
                this.toolId = Integer.valueOf(m.get("id").toString());
                return;
            }
        }
        throw new IllegalStateException("No tool with an 'id' found in /tools response: " + r.asString());
    }

    @When("I send GET request to that tool")
    public void i_send_get_request_to_that_tool() {
        assertThat(toolId).as("toolId should be picked first").isNotNull();
        response = given()
                .spec(Specs.request())
                .when()
                .get("/tools/" + toolId);
    }
    
    @After
    public void cleanupCreatedOrder() {
        if (createdOrderId != null && !createdOrderId.isBlank() && !"does-not-exist".equals(createdOrderId)) {
            try {
                given()
                    .spec(Specs.request())
                    .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                    .when()
                    .delete("/orders/" + createdOrderId)
                    .then()
                    .statusCode(org.hamcrest.Matchers.anyOf(org.hamcrest.Matchers.is(204), org.hamcrest.Matchers.is(404)));
            } catch (Exception ignored) {
                // ne rušimo suite zbog cleanup-a
            } finally {
                createdOrderId = null;
            }
        }
    }
    
    @When("I fetch that order")
    public void i_fetch_that_order() {
        String orderId = OrderState.getLastCreatedOrderId();
        assertThat(orderId).as("An order must be created first to fetch it").isNotBlank();
        response = OrdersApi.get(orderId);
    }


    @When("I delete that order")
    public void i_delete_that_order() {
        String orderId = OrderState.getLastCreatedOrderId();
        assertThat(orderId).as("An order must be created first to delete it").isNotBlank();
        response = OrdersApi.delete(orderId);
    }

    
    @Then("the response status code should be one of {int} or {int}")
    public void the_response_status_code_should_be_one_of_or(Integer a, Integer b) {
        assertThat(response.getStatusCode()).isIn(a, b);
    }
    
    @When("I send DELETE request to {string}")
    public void i_send_delete_request_to(String path) {
        response = given()
                .spec(Specs.request()) // no auth on purpose
                .when()
                .delete(path);
    }

    
 // --- PATCH helpers ---

    @When("I update that order status to {string}")
    public void i_update_that_order_status_to(String newStatus) {
        String orderId = OrderState.getLastCreatedOrderId();
        assertThat(orderId).as("An order must be created first to update it").isNotBlank();
        response = OrdersApi.updateStatusWithFallback(orderId, newStatus);
    }



    @Then("the order status should be {string}")
    public void the_order_status_should_be(String expected) {
        String actual = response.jsonPath().getString("status");
        assertThat(actual).as("Updated order status should match").isEqualTo(expected);
    }

    // Generic authorized PATCH with free-form body (useful for negative tests)
    @When("I send an authorized PATCH request to {string} with body:")
    public void i_send_an_authorized_patch_request_to_with_body(String path, String docString) {
        response = given()
                .spec(com.example.api.utils.RequestUtils.authSpec())
                .body(docString)
                .when()
                .patch(path);
    }

    @When("I update that order customer name to {string}")
    public void i_update_that_order_customer_name_to(String newName) {
        String orderId = OrderState.getLastCreatedOrderId();
        assertThat(orderId).as("An order must be created first to update it").isNotBlank();
        response = OrdersApi.updateCustomerName(orderId, newName);
    }

    
    @When("I send PATCH request to that order with body:")
    public void i_send_patch_request_to_that_order_with_body(String docString) {
        String orderId = com.example.api.support.OrderState.getLastCreatedOrderId();
        org.assertj.core.api.Assertions.assertThat(orderId)
                .as("An order must exist before PATCHing it")
                .isNotBlank();

        response = io.restassured.RestAssured.given()
                .spec(com.example.api.specs.Specs.request()) // NO auth header here on purpose
                .body(docString)
                .when()
                .patch("/orders/" + orderId);
    }


    @Then("the order customer name should be {string}")
    public void the_order_customer_name_should_be(String expected) {
        String orderId = com.example.api.support.OrderState.getLastCreatedOrderId();
        org.assertj.core.api.Assertions.assertThat(orderId)
                .as("An order must exist before verification")
                .isNotBlank();

        io.restassured.response.Response r = io.restassured.RestAssured.given()
                .spec(com.example.api.utils.RequestUtils.authSpec())
                .when()
                .get("/orders/" + orderId);

        org.assertj.core.api.Assertions.assertThat(r.getStatusCode()).isEqualTo(200);
        String actual = r.jsonPath().getString("customerName");
        org.assertj.core.api.Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Then("each order item should have required fields")
    public void each_order_item_should_have_required_fields() {
        // Basic shape: array of objects
        List<Map<String, Object>> items = response.jsonPath().getList("$");
        assertThat(items)
                .as("Orders list must be an array")
                .isNotNull();

        for (Map<String, Object> it : items) {
            // orderId: non-empty string
            Object orderId = it.get("orderId");
            assertThat(orderId)
                    .as("orderId must be present")
                    .isInstanceOf(String.class);
            assertThat((String) orderId)
                    .as("orderId must be non-blank")
                    .isNotBlank();

            // toolId: numeric
            Object toolId = it.get("toolId");
            assertThat(toolId)
                    .as("toolId must be present")
                    .isInstanceOf(Number.class);

            // customerName: non-empty string
            Object customerName = it.get("customerName");
            assertThat(customerName)
                    .as("customerName must be present")
                    .isInstanceOf(String.class);
            assertThat((String) customerName)
                    .as("customerName must be non-blank")
                    .isNotBlank();

            // createdAt: optional but, if present, should be non-empty string
            if (it.containsKey("createdAt")) {
                Object createdAt = it.get("createdAt");
                assertThat(createdAt)
                        .as("createdAt, if present, should be a non-blank string")
                        .isInstanceOf(String.class);
                assertThat((String) createdAt).isNotBlank();
            }
        }
    }

    
    
}
