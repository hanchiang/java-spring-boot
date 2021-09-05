package com.han.springapp.firstjavaproject.ui.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

// TODO: Find a way to inject UserRepository to delete data after each test
class UserControllerIT {
    private final String CONTEXT_PATH = "/first-java-project";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUser() {
    }

    // TODO: Complete the rest of the integration tests
    @Test
    void createUser() {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "first name");
        userDetails.put("lastName", "last name");
        userDetails.put("email", "email");
        userDetails.put("password", "password");

        Response response =  given().contentType("application/json")
                .accept("application/json")
                .body(userDetails)
                .when()
                .post(CONTEXT_PATH + "/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response();

        String userId = response.jsonPath().getString("userId");
        assertNotNull(userId);
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getUsers() {
    }
}