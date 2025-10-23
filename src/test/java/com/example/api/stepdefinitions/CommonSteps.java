package com.example.api.stepdefinitions;

import com.example.api.specs.Specs;
import com.example.api.utils.TokenManager;
import io.restassured.response.Response;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    private Response response;
    private String token;

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
        response = given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .when()
                .get(path);
    }

    @When("I create a new order with a valid tool id and customer name")
    public void i_create_a_new_order_with_valid_tool_id_and_customer_name() {
        int toolId = 1;
        String payload = String.format("{\"toolId\": %d, \"customerName\": \"John Doe\"}", toolId);

        response = given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(payload)
                .when()
                .post("/orders");
    }

    @Then("the response should match schema {string}")
    public void the_response_should_match_schema(String schemaPath) {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(schemaPath));
    }


    @When("I send an authorized POST request to {string} with body:")
    public void i_send_an_authorized_post_request_to_with_body(String path, String docString) {
        response = given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(docString)
                .when()
                .post(path);
    }

    @When("I send an authorized DELETE request to {string}")
    public void i_send_an_authorized_delete_request_to(String path) {
        response = given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
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
}
