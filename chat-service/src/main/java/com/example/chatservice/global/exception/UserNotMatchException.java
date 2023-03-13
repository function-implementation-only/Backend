package com.example.chatservice.global.exception;

public class UserNotMatchException extends RuntimeException {

    public UserNotMatchException(String s) {
        super(s);
    }
}
