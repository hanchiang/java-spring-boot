package com.han.springapp.firstjavaproject.ui.controller;

import com.han.springapp.firstjavaproject.shared.dto.UserDto;
import com.han.springapp.firstjavaproject.ui.model.response.OperationNames;
import com.han.springapp.firstjavaproject.ui.model.response.OperationStatus;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIT {
    private final String CONTEXT_PATH = "/first-java-project";
    private static String authHeader;
    private static String userId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    @Order(1)
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
    @Order(2)
    void login() {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", "email");
        loginDetails.put("password", "password");

        Response response = given().contentType("application/json")
                .accept("application/json")
                .body(loginDetails)
                .when()
                .post(CONTEXT_PATH + "/users/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        authHeader = response.header("Authorization");
        userId = response.header("User ID");

        assertNotNull(authHeader);
        assertNotNull(userId);
    }

    @Test
    @Order(3)
    void getUsers() {
        Response response = given().header("Authorization", authHeader)
                .when()
                .get(CONTEXT_PATH + "/users")
                .then()
                .extract()
                .response();

        List<UserDto> users = response.jsonPath().getList("", UserDto.class);
        assertEquals(1, users.size());
        assertEquals(userId, users.get(0).getUserId());
        assertEquals("first name", users.get(0).getFirstName());
        assertEquals("last name", users.get(0).getLastName());
        assertEquals("email", users.get(0).getEmail());
    }

    @Test
    @Order(4)
    void getUser() {
        Response response = given()
                .header("Authorization", authHeader)
                .pathParam("id", userId)
                .contentType("application/json")
                .accept("application/json")
                .when()
                .get(CONTEXT_PATH + "/users/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String userId = response.jsonPath().getString("userId");
        String email = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");
        assertNotNull(userId);
        assertNotNull(email);
        assertNotNull(firstName);
        assertNotNull(lastName);
    }

    @Test
    @Order(5)
    void updateUser() {
        Map<String, String> updateUserDetails = new HashMap<>();
        updateUserDetails.put("firstName", "new first name");
        updateUserDetails.put("lastName", "new last name");

        Response response = given()
                .header("Authorization", authHeader)
                .pathParam("id", userId)
                .contentType("application/json")
                .accept("application/json")
                .body(updateUserDetails)
                .when()
                .put(CONTEXT_PATH + "/users/{id}")
                .then()
                .extract()
                .response();

        String userId = response.jsonPath().getString("userId");
        String email = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");

        assertEquals(200, response.getStatusCode());
        assertNotNull(userId);
        assertNotNull(email);
        assertNotNull(firstName);
        assertNotNull(lastName);

        assertEquals("new first name", firstName);
        assertEquals("new last name", lastName);
    }

    @Test
    @Order(6)
    void deleteUser() {
        Response response = given().header("Authorization", authHeader)
                .pathParam("id", userId)
                .when()
                .delete(CONTEXT_PATH + "/users/{id}")
                .then()
                .extract()
                .response();

        assertEquals(200, response.getStatusCode());

        String operationName = response.jsonPath().getString("operationName");
        String operationResult = response.jsonPath().getString("operationResult");
        assertEquals(OperationNames.DELETE.name(), operationName);
        assertEquals(OperationStatus.SUCCESS.name(), operationResult);
    }
}