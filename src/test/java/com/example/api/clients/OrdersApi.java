package com.example.api.clients;

import com.example.api.models.orders.OrderCreateRequest;
import com.example.api.models.orders.OrderResponse;
import com.example.api.specs.Specs;
import com.example.api.utils.TokenManager;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** Thin client around /orders endpoints to remove duplication in step defs. */
public final class OrdersApi {
    private OrdersApi() {}

    /** Authorized GET /orders */
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

    /** Authorized GET /orders/{orderId} -> DTO */
    public static OrderResponse getTyped(String orderId) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(200)
                .extract()
                .as(OrderResponse.class);
    }

    /** Authorized POST /orders using DTO body (recommended) */
    public static Response create(OrderCreateRequest req) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(req)
                .when()
                .post("/orders");
    }

    /** Authorized POST /orders using DTO body -> DTO */
    public static OrderResponse createTyped(OrderCreateRequest req) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(req)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .as(OrderResponse.class);
    }

    /** Backward compatibility: accepts raw params but uses DTO internally */
    public static Response create(int toolId, String customerName) {
        OrderCreateRequest req = new OrderCreateRequest();
        req.setToolId(toolId);
        req.setCustomerName(customerName);
        return create(req);
    }

    /** Backward compatibility: raw params -> DTO return */
    public static OrderResponse createTyped(int toolId, String customerName) {
        OrderCreateRequest req = new OrderCreateRequest();
        req.setToolId(toolId);
        req.setCustomerName(customerName);
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

    /** Authorized PATCH /orders/{orderId} sending raw JSON body */
    public static Response patchBody(String orderId, String rawJsonBody) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(rawJsonBody)
                .when()
                .patch("/orders/" + orderId);
    }

    /** Authorized PATCH /orders/{orderId}?key=value */
    public static Response patchQuery(String orderId, Map<String, ?> queryParams) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .queryParams(queryParams)
                .when()
                .patch("/orders/" + orderId);
    }

    /** Update only customerName */
    public static Response updateCustomerName(String orderId, String newName) {
        String body = String.format("{\"customerName\": \"%s\"}", newName);
        return patchBody(orderId, body);
    }

    /**
     * Update status to value. Some environments reject JSON body and only accept query param.
     * Try body first; if 400 with known message, retry using query param.
     */
    public static Response updateStatusWithFallback(String orderId, String status) {
        String body = String.format("{\"status\": \"%s\"}", status);
        Response first = patchBody(orderId, body);
        if (first.getStatusCode() == 400 && first.asString().contains("No valid fields to update")) {
            System.out.println("[OrdersApi] Fallback to query param PATCH...");
            return patchQuery(orderId, Map.of("status", status));
        }
        return first;
    }
}
