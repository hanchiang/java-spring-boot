package com.han.springapp.firstjavaproject.exception;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message) {
        super(message);
    }
}
