package com.example.chatservice.service;

import com.example.chatservice.config.security.user.UserDetailsImpl;
import com.example.chatservice.dto.ChatDto.CreateResponse;
import com.example.chatservice.dto.ChatDto.Response;
import com.example.chatservice.dto.ChatRoomDto;
import com.example.chatservice.dto.ChatRoomDto.CreateRequest;
import com.example.chatservice.model.ChatMessage;
import com.example.chatservice.model.ChatRoom;
import com.example.chatservice.repository.ChatRoomRepository;
import com.example.chatservice.exception.chatroom.DuplicatedChatRoomException;
import com.example.chatservice.exception.UserNotMatchException;
import com.example.chatservice.exception.chatroom.ChatRoomNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;


    @Transactional
    public CreateResponse createRoom(@Valid CreateRequest request, UserDetailsImpl userDetails) {
        if(checkDuplicatedForCreate(request,userDetails.getAccount())){
            ChatRoom room = chatRoomRepository.findByReceiverAndSender(request.getTargetEmail(),userDetails.getAccount());
            return CreateResponse.builder().name(room.getRoomName()).build();
        }else {
            ChatRoom chatRoom = request.toEntity(userDetails.getAccount(),
                request.getTargetEmail());
            chatRoomRepository.save(chatRoom);
            return CreateResponse.builder().name(chatRoom.getRoomName()).build();
        }
    }

    private boolean checkDuplicatedForCreate(CreateRequest request, String sender) {
        return chatRoomRepository.existsByReceiverAndSender(sender,request.getTargetEmail());
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
        for (ChatMessage chat : chats) {
            Response response = Response.builder()
                .chat(chat)
                .build();
            responseList.add(response);
        }
        return responseList;
    }

    @Transactional(readOnly = true)
    public Page<ChatRoomDto.Response> getChatRoomList(String email, Pageable pageable) {
        Page<ChatRoom> rooms = new PageImpl<>(Collections.emptyList());
        rooms = chatRoomRepository.findAllByEmail(email, pageable);
        return new PageImpl<>(entityToListDto(rooms,email), pageable, rooms.getTotalElements());
    }

    private List<ChatRoomDto.Response> entityToListDto(Page<ChatRoom> rooms, String email) {
        return rooms.stream().map(room -> ChatRoomDto.Response.builder()
                .room(room)
                .unreadMessageCount(getUnreadCount(room))
                .latestChatMessage(getLatestChatMessage(room))
                .nickname(getReceiverNickname(room,email))
                .build())
            .collect(Collectors.toList());
    }

    private String getReceiverNickname(ChatRoom room, String email) {
        String nickname ="";
        if(room.getReceiver().equals(email)){
            return room.getSender();
        }else return room.getReceiver();
    }

    private String getLatestChatMessage(ChatRoom room) {
        Optional<ChatMessage> message = room.getChats().stream()
            .reduce((o1, o2) -> o1.getCreatedAt().isAfter(o2.getCreatedAt()) ? o1 : o2);
        return message.map(ChatMessage::getMessage).orElse(null);
    }

    private Long getUnreadCount(ChatRoom room) {
        return room.getChats().stream().filter(chat -> !chat.isMessageCheckStatus())
            .count();
    }

    private ChatRoom getRoom(String roomName) {
        return chatRoomRepository.findChatRoomByRoomName(roomName)
            .orElseThrow(() -> new ChatRoomNotFoundException("존재하지않는 채팅방입니다."));

    }
}
