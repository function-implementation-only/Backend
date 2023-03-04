package com.example.chatservice.model;

import com.example.chatservice.dto.ChatDto.CreateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@CrossOrigin("*")
public class ChatMessage extends BaseTimeEntity{
    @Id
    @Column(name="message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private ChatRoom room;

    private String sender;

    private String message;

    private boolean messageCheckStatus;

    public static ChatMessage createMessage(CreateRequest request, ChatRoom room) {
        return ChatMessage.builder()
            .sender(request.getSender())
            .message(request.getMessage())
            .room(room)
            .build();
    }
}
