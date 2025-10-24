package com.example.api.support;

import com.example.api.specs.Specs;
import com.example.api.utils.TokenManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import static io.restassured.RestAssured.given;

/** Global hooks: reset scenario state and cleanup created orders. */
public class Hooks {

    @Before
    public void beforeScenario() {
        // Ensure scenario starts clean
        OrderState.clear();
    }

    // Runs after scenarios that manipulate orders (tag @orders) or explicitly opt-in via @cleanup
    @After("@orders or @cleanup")
    public void cleanupOrder() {
        String orderId = OrderState.getLastCreatedOrderId();
        if (orderId == null || orderId.isBlank()) return;

        try {
            int code = given()
                    .spec(Specs.request())
                    .header("Authorization", "Bearer " + TokenManager.getOrCreateToken())
                    .when()
                    .delete("/orders/" + orderId)
                    .getStatusCode();

            // Be tolerant to API variations (200/204 on success, 404 if already gone)
            if (code != 200 && code != 204 && code != 404) {
                System.out.println("[Hooks] Unexpected DELETE /orders/" + orderId + " status: " + code);
            }
        } catch (Exception e) {
            System.out.println("[Hooks] Cleanup failed for orderId=" + orderId + " -> " + e.getMessage());
        } finally {
            OrderState.clear();
        }
    }
}
