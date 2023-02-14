package com.example.chatservice.service;

import com.example.chatservice.dto.ChatDto.CreateRequest;
import com.example.chatservice.model.ChatMessage;
import com.example.chatservice.model.ChatRoom;
import com.example.chatservice.repository.ChatMessageRepository;
import com.example.chatservice.repository.ChatRoomRepository;
import com.example.exception.chatroom.ChatRoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public String save(CreateRequest request) {
        ChatRoom room = getRoom(request.getRoomId());
        ChatMessage message = ChatMessage.createMessage(request,room);
        chatMessageRepository.save(message);
        return room.getRoomName();
    }

    private ChatRoom getRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다."));
    }

}
