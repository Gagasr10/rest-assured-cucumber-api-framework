package com.example.api.utils;



import com.example.api.specs.Specs;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/** Small helpers to keep step defs tidy. */

public final class RequestUtils {
    private RequestUtils() {}

    /** Returns base spec + Authorization header with a valid token. */
    public static RequestSpecification authSpec() {
        return Specs.request()
                .header("Authorization", "Bearer " + TokenManager.getOrCreateToken());
    }

    /** Fetches the first valid tool id from /tools (throws if none). */
    public static int pickFirstValidToolId() {
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
                return Integer.parseInt(m.get("id").toString());
            }
        }
        throw new IllegalStateException("No tool with an 'id' found in /tools response: " + r.asString());
    }
}
