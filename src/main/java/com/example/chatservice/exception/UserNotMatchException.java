package com.example.exception;

public class UserNotMatchException extends RuntimeException {

    public UserNotMatchException(String s) {
        super(s);
    }
}
