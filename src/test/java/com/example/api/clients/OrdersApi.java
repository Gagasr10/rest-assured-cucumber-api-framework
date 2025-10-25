package com.example.api.clients;

import com.example.api.models.orders.OrderCreateRequest;
import com.example.api.models.orders.OrderResponse;
import com.example.api.specs.Specs;
import com.example.api.utils.TokenManager;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public final class OrdersApi {

    private OrdersApi() {}

    /** Authorized GET /orders - list */
    public static Response getAll() {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .when()
                .get("/orders");
    }

    /** Authorized GET /orders/{orderId} */
    public static Response get(String orderId) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .when()
                .get("/orders/" + orderId);
    }

    /** Typed GET /orders/{orderId} returning DTO */
    public static OrderResponse getTyped(String orderId) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .when()
                .get("/orders/" + orderId)
                .as(OrderResponse.class);
    }

    /** Authorized POST /orders using DTO body */
    public static Response create(OrderCreateRequest req) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(req)
                .when()
                .post("/orders");
    }

    public static Response create(int toolId, String customerName) {
        OrderCreateRequest req = new OrderCreateRequest(toolId, customerName);
        return create(req);
    }


    /** Typed POST - returns DTO */
    public static OrderResponse createTyped(OrderCreateRequest req) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(req)
                .when()
                .post("/orders")
                .as(OrderResponse.class);
    }

    /** Backward compatibility typed version */
    public static OrderResponse createTyped(int toolId, String customerName) {
        OrderCreateRequest req = new OrderCreateRequest(toolId, customerName);
        return createTyped(req);
    }

    /** Authorized DELETE /orders/{orderId} */
    public static Response delete(String orderId) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .when()
                .delete("/orders/" + orderId);
    }

    /** Authorized PATCH body */
    public static Response patchBody(String orderId, String rawJsonBody) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(rawJsonBody)
                .when()
                .patch("/orders/" + orderId);
    }

    /** Authorized PATCH query params */
    public static Response patchQuery(String orderId, Map<String, ?> queryParams) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .queryParams(queryParams)
                .when()
                .patch("/orders/" + orderId);
    }

    /** Update customer name */
    public static Response updateCustomerName(String orderId, String newName) {
        String body = String.format("{\"customerName\": \"%s\"}", newName);
        return patchBody(orderId, body);
    }

    /** Update status with fallback */
    public static Response updateStatusWithFallback(String orderId, String status) {
        String body = String.format("{\"status\": \"%s\"}", status);
        Response first = patchBody(orderId, body);

        if (first.getStatusCode() == 400 &&
                first.asString().contains("No valid fields to update")) {
            return patchQuery(orderId, Map.of("status", status));
        }
        return first;
    }
}
