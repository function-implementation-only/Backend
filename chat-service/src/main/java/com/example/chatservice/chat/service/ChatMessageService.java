package com.example.chatservice.chat.service;

import com.example.chatservice.chat.dto.ChatDto.CreateRequest;
import com.example.chatservice.kafka.ChatProducer;
import com.example.chatservice.kafka.dto.ChatSimpleDto;
import com.example.chatservice.chat.entity.ChatMessage;
import com.example.chatservice.chat.entity.ChatRoom;
import com.example.chatservice.chat.repository.ChatMessageRepository;
import com.example.chatservice.chat.repository.ChatRoomRepository;
import com.example.chatservice.global.exception.chatroom.ChatRoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ChatProducer chatProducer;

    public String save(CreateRequest request) {
        ChatRoom room = getRoom(request.getRoomId());
        ChatMessage message = ChatMessage.createMessage(request, room);
        /*message db 저장 단계 with kafka*/
//        chatMessageRepository.save(message);
        ChatSimpleDto chatSimpleDto = ChatSimpleDto.builder()
                .message(message.getMessage())
                .sender(message.getSender())
                .message_check_status((byte) 0)
                .room_id(request.getRoomId())
                .build();

        /*Kafka connect를 통해 Data를 DB에 전달*/
        chatProducer.send("chat_message", chatSimpleDto);
        return room.getRoomName();

    }

    private ChatRoom getRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다."));
    }

}
