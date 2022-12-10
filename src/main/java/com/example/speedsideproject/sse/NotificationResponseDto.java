package com.example.speedsideproject.sse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponseDto {
    private Long notificationId;

    private String message;

    private Long articlesId;

    private Boolean readStatus;

    private AlarmType alarmType;

    private String title;

//    private String createdAt;

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.message = notification.getMessage();
        this.articlesId = notification.getUrl();
        this.readStatus = notification.getReadState();
        this.title = notification.getTitle();
        this.alarmType = notification.getAlarmType();
    }
}
