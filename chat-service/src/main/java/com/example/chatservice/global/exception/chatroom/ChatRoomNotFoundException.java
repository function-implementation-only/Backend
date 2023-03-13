package com.example.chatservice.global.exception.chatroom;

public class ChatRoomNotFoundException extends NullPointerException{

    public ChatRoomNotFoundException(String message) {
        super(message);
    }
}
