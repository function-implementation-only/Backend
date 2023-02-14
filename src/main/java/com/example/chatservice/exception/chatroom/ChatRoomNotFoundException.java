package com.example.exception.chatroom;

public class ChatRoomNotFoundException extends NullPointerException{

    public ChatRoomNotFoundException(String message) {
        super(message);
    }
}
