package com.example.chatservice.service;

import com.example.chatservice.config.security.user.UserDetailsImpl;
import com.example.chatservice.dto.ChatDto.Response;
import com.example.chatservice.dto.ChatRoomDto;
import com.example.chatservice.dto.ChatRoomDto.CreateRequest;
import com.example.chatservice.model.ChatMessage;
import com.example.chatservice.model.ChatRoom;
import com.example.chatservice.repository.ChatRoomRepository;
import com.example.exception.UserNotMatchException;
import com.example.exception.chatroom.ChatRoomNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;


    @Transactional
    public String createRoom(@Valid CreateRequest request, UserDetailsImpl userDetails) {
        ChatRoom chatRoom = request.toEntity(userDetails.getAccount(), request.getTargetEmail());
        chatRoomRepository.save(chatRoom);
        return chatRoom.getRoomName();
    }

    @Transactional
    public void deleteRoom(String roomName, UserDetailsImpl userDetails) {
        ChatRoom room = getRoom(roomName);
        String email = userDetails.getAccount();

        if (!email.equals(room.getReceiver()) && !email.equals(room.getSender())) {
            throw new UserNotMatchException("본인이 참여중인 채팅방만 삭제할 수 있습니다");
        }
        room.changeStatus();
        chatRoomRepository.save(room);
    }

    @Transactional
    public ChatRoomDto.Response getChatRoomDetail(String roomName, String account) {
        ChatRoom room = getRoom(roomName);
        room.getChats().stream().filter(chat -> !chat.getSender().equals(account))
            .forEach(chatMessage -> chatMessage.setMessageCheckStatus(true));
        chatRoomRepository.save(room);

        return ChatRoomDto.Response.builder()
            .room(room)
            .chats(chatConverToResponseDto(room.getChats()))
            .build();
    }

    private List<Response> chatConverToResponseDto(List<ChatMessage> chats) {
        List<Response> responseList = new ArrayList<>();
        for(ChatMessage chat : chats){
            Response response = Response.builder()
                .chat(chat)
                .build();
            responseList.add(response);
        }
        return responseList;
    }

    private ChatRoom getRoom(String roomName) {
        return chatRoomRepository.findChatRoomByRoomName(roomName)
            .orElseThrow(() -> new ChatRoomNotFoundException("존재하지않는 채팅방입니다."));

    }
}
