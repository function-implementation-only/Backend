package com.example.chatservice.exception.chatroom;

public class DuplicatedChatRoomException extends RuntimeException {
    public DuplicatedChatRoomException(String message) {
        super(message);
    }
}
