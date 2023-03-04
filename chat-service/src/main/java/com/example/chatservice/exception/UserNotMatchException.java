package com.example.chatservice.exception;

public class UserNotMatchException extends RuntimeException {

    public UserNotMatchException(String s) {
        super(s);
    }
}
