package com.han.springapp.firstjavaproject.shared;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

// Load ApplicationContext
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateUserId() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertTrue(userId.length() == 30);
        assertTrue(!userId.equalsIgnoreCase(userId2));
    }

    @Test
    void hasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("user id");
        boolean isExpired = Utils.hasTokenExpired(token);

        assertTrue(!isExpired);
    }

    @Test
    void hasTokenExpired() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIGlkIiwiZXhwIjoxNjI4ODE4MzcyfQ.NxAkSbMlXdfAS5V5KE-UolCB1r458bAu56dwUj4fDVfUSIDyuqZoVY4VPZ9Cx4TtyWS3ZqUAR06PxSY1RIg-eA";
        boolean isExpired = utils.hasTokenExpired(expiredToken);

        assertTrue(isExpired);
    }
}