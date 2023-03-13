package com.example.speedsideproject.domain.notification.service;


import com.example.speedsideproject.domain.account.repository.AccountRepository;
import com.example.speedsideproject.domain.applyment.entity.Applyment;
import com.example.speedsideproject.domain.notification.dto.NotificationDto;
import com.example.speedsideproject.domain.notification.entity.Notification;
import com.example.speedsideproject.global.error.CustomException;
import com.example.speedsideproject.global.error.ErrorCode;
import com.example.speedsideproject.global.security.user.UserDetailsImpl;
import com.example.speedsideproject.domain.notification.repository.EmitterRepositoryImpl;
import com.example.speedsideproject.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepositoryImpl emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String email, String lastEventId) {

        String emitterId = makeTimeIncludeId(email);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));


        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(email);
        sendNotification(emitter, eventId, emitterId,
                "EventStream Created. [userId=" + email + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, email, emitterId, emitter);
        }
        return emitter;
    }

    private String makeTimeIncludeId(String email) {
        return email + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId,
                                  Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String email, String emitterId,
                              SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByAccountEmail(
                String.valueOf(email));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(
                        entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    public void send(NotificationDto.CreateRequest createRequest) {
        String email = createRequest.getReceiver();
        Notification notification = notificationRepository.save(createNotification(
                createRequest.getReceiver(), createRequest.getSender(),
                createRequest.getApplyment()));

        NotificationDto.NotificationResponse response = new NotificationDto.NotificationResponse(notification, accountRepository.findByEmail(notification.getSender()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)).getNickname());
        String eventId = email + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByAccountEmail(
                email);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, response);
                    sendNotification(emitter, eventId, key, response);
                }
        );
    }

    private Notification createNotification(String receiver, String sender, Applyment applyment) {
        return Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .applyment(applyment)
                .isRead(false)
                .build();
    }

    /*읽지않은알림 리스트 불러오기*/
    public List<?> getNotificationList(UserDetailsImpl userDetails) {
        return notificationRepository.findAllByReceiverAndIsReadFalse(userDetails.getAccount().getEmail()).stream().map(notification -> new NotificationDto.NotificationResponse(notification, accountRepository.findByEmail(notification.getSender()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)).getNickname())).collect(Collectors.toList());
    }

    private final AccountRepository accountRepository;

}