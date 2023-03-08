package com.example.speedsideproject.sse.dto;

import com.example.speedsideproject.applyment.entity.Applyment;
import com.example.speedsideproject.sse.Notification;
import lombok.*;

import java.time.LocalDateTime;

public class NotificationDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateRequest {

        private String sender;

        private String receiver;

        private Long url;

        private Applyment applyment;

    }

    @Getter
    @Data
    public static class NotificationResponse {
        private String sender;
        private String senderNickname;
        private String receiver;
        private Long applymentId;
        private LocalDateTime created_at;

        public NotificationResponse(Notification notification, String senderNickname) {
            this.sender = notification.getSender();
            this.senderNickname = senderNickname;
            this.receiver = notification.getReceiver();
            this.applymentId = notification.getApplyment().getId();
            this.created_at = notification.createdAt;
        }
    }
}