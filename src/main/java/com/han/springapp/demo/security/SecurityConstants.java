package com.han.springapp.demo.security;

import com.han.springapp.demo.SpringApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 24 * 10; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";

    public static String getTokenSecret() {
        // Remember to use camelCase for the bean name
        AppProperties appProperties = (AppProperties)SpringApplicationContext.getBean("appProperties");
        return appProperties.getTokenSecret();
    }
}
