package com.han.springapp.firstjavaproject.ui.controller;

import com.han.springapp.firstjavaproject.shared.dto.UserDto;
import com.han.springapp.firstjavaproject.ui.model.request.AddressSignupRequest;
import com.han.springapp.firstjavaproject.ui.model.response.OperationNames;
import com.han.springapp.firstjavaproject.ui.model.response.OperationStatus;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
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

    Map<String, Object> getUserDetails() {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "first name");
        userDetails.put("lastName", "last name");
        userDetails.put("email", "email");
        userDetails.put("password", "password");

        List<AddressSignupRequest> addresses = getSignUpAddresses();
        userDetails.put("addresses", addresses);

        return userDetails;
    }

    List<AddressSignupRequest> getSignUpAddresses() {
        List<AddressSignupRequest> addresses = new ArrayList<>();
        AddressSignupRequest address1 = new AddressSignupRequest();
        address1.setCity("Vancouver");
        address1.setCountry("Canada");
        address1.setStreetName("Street name");
        address1.setPostalCode("123456");
        address1.setType("billing");

        AddressSignupRequest address2 = new AddressSignupRequest();
        address2.setCity("Vancouver");
        address2.setCountry("Canada");
        address2.setStreetName("Street name");
        address2.setPostalCode("123456");
        address2.setType("shipping");

        return addresses;
    }

    @Test
    @Order(1)
    void createUser() {
        Map<String, Object> userDetails = getUserDetails();

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
        Map<String, Object> userDetails = getUserDetails();

        System.out.println(users);
        assertEquals(1, users.size());
        assertEquals(userId, users.get(0).getUserId());
        assertEquals(userDetails.get("firstName"), users.get(0).getFirstName());
        assertEquals(userDetails.get("lastName"), users.get(0).getLastName());
        assertEquals(userDetails.get("email"), users.get(0).getEmail());
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

        String localUserId = response.jsonPath().getString("userId");
        String email = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");

        Map<String, Object> userDetails = getUserDetails();

        assertEquals(userId, localUserId);
        assertEquals(userDetails.get("firstName"), firstName);
        assertEquals(userDetails.get("lastName"), lastName);
        assertEquals(userDetails.get("email"), email);
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

        String localUserId = response.jsonPath().getString("userId");
        String email = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");

        Map<String, Object> userDetails = getUserDetails();

        assertEquals(200, response.getStatusCode());

        assertEquals(userId, localUserId);
        assertEquals(updateUserDetails.get("firstName"), firstName);
        assertEquals(updateUserDetails.get("lastName"), lastName);
        assertEquals(userDetails.get("email"), email);
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

    // TODO: getUserAddress, getUserAddresses
}