package com.example.api.clients;

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

    /** Authorized POST /orders with simple JSON body */
    public static Response create(int toolId, String customerName) {
        String payload = String.format("{\"toolId\": %d, \"customerName\": \"%s\"}", toolId, customerName);
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(payload)
                .when()
                .post("/orders");
    }

    /** Authorized DELETE /orders/{orderId} */
    public static Response delete(String orderId) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .when()
                .delete("/orders/" + orderId);
    }

    /** Authorized PATCH /orders/{orderId} with raw body */
    public static Response patchBody(String orderId, String rawJsonBody) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .body(rawJsonBody)
                .when()
                .patch("/orders/" + orderId);
    }

    /** Authorized PATCH /orders/{orderId}?key=value (for servers that accept query form) */
    public static Response patchQuery(String orderId, Map<String, ?> queryParams) {
        return given()
                .spec(Specs.request())
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                .queryParams(queryParams)
                .when()
                .patch("/orders/" + orderId);
    }

    /** Update customerName with body first; no fallback needed for name (server accepts body). */
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
            System.out.println("[OrdersApi] Body PATCH returned 400; retrying as query param ...");
            return patchQuery(orderId, Map.of("status", status));
        }
        return first;
    }
}
