package com.example.speedsideproject.websocket.dto;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class ChatRoomDTO {
    private String roomId;
    private String name;

    private Set<WebSocketSession> sessions = new HashSet<>();

    //WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션
    public static ChatRoomDTO create(String name){
        ChatRoomDTO room = new ChatRoomDTO();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }
}
