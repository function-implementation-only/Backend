package com.example.speedsideproject.websocket.service;

import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.error.ErrorCode;
import com.example.speedsideproject.websocket.domain.Chat;
import com.example.speedsideproject.websocket.domain.Room;
import com.example.speedsideproject.websocket.dto.ChatReqDto;
import com.example.speedsideproject.websocket.repository.ChatRepository;
import com.example.speedsideproject.websocket.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;



    /**
     * 채팅 생성
     */
    public Chat createChat(Long roomId, ChatReqDto chatReqDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );//방 찾기 -> 없는 방일 경우 여기서 예외처리
        Chat chat = Chat.builder()
                .room(room)
                .sender(chatReqDto.getSender())
                .message(chatReqDto.getMessage())
                .build();
        chatRepository.save(chat);
        return chat;
    }

    /**
     * 채팅방 채팅내용 불러오기
     * @param roomId 채팅방 id
     */
    public List<Chat> findAllChatByRoomId(Long roomId) {
        return chatRepository.findAllByRoom_PostId(roomId);
    }

}
