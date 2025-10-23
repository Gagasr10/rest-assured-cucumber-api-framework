package com.example.api.utils;

import com.example.api.specs.Specs;
import io.restassured.response.Response;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class TokenManager {

    private static String token;

    public static String getOrCreateToken() {
        if (token == null || token.isBlank()) {
            String clientName = "framework-client";
            String clientEmail = "qa+" + UUID.randomUUID() + "@example.com";

            Response resp = given()
                    .spec(Specs.request())
                    .body("{\"clientName\":\""+ clientName + "\",\"clientEmail\":\""+ clientEmail +"\"}")
                    .when()
                    .post("/api-clients")
                    .then()
                    .statusCode(201)
                    .extract().response();

            String maybe = resp.path("accessToken");
            if (maybe == null || maybe.isBlank()) {
                throw new IllegalStateException("accessToken not found in /api-clients response: " + resp.asString());
            }
            token = maybe;
        }
        return token;
    }
}
