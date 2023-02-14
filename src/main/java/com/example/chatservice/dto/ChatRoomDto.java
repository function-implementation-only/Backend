package com.example.chatservice.dto;

import com.example.chatservice.dto.ChatDto.Response;
import com.example.chatservice.model.ChatMessage;
import com.example.chatservice.model.ChatRoom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatRoomDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateRequest {

        @NotBlank(message = "메세지를 전달할 상대방은 필수입니다.")
        private String targetEmail;

        public ChatRoom toEntity(String account, String targetEmail) {
            return ChatRoom.builder()
                .roomName(UUID.randomUUID().toString())
                .sender(account)
                .receiver(targetEmail)
                .build();
        }
    }

    @Getter
    public static class Response {

        private Long roomId; //방 번호
        private String roomName; // 방 이름
        private List<ChatDto.Response> chatList;

        // getChatroomDetail 메서드에서는 response에 할당되지 않음
        private Long unreadMessageCount;
        private String latestChatMessage;


        @Builder
        Response(ChatRoom room, List<ChatDto.Response> chats, Long unreadMessageCount
            , String latestChatMessage) {
            this.roomId = room.getId();
            this.roomName = room.getRoomName();
            this.unreadMessageCount = unreadMessageCount;
            this.latestChatMessage = latestChatMessage;
            this.chatList = chats;
        }
    }

}
