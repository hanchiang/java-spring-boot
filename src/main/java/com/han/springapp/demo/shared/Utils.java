package com.han.springapp.demo.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Component.html
@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder retVal = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            retVal.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return retVal.toString();
    }
}
