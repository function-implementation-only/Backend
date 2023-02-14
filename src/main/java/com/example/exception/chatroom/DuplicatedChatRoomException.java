package com.example.exception.chatroom;

public class DuplicatedChatRoomException extends RuntimeException {
    public DuplicatedChatRoomException(String message) {
        super(message);
    }
}
