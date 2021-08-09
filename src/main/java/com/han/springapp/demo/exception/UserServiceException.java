package com.han.springapp.demo.exception;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message) {
        super(message);
    }
}
