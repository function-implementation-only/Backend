package com.example.chatservice.sse;

import com.example.chatservice.sse.dto.NotificationDto.CreateRequest;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepositoryImpl emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String email, String lastEventId) {
        log.info("1");
        String emitterId = makeTimeIncludeId(email);
        log.info("2");
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        log.info("3");
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        log.info("4");
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        log.info("5");

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(email);
        sendNotification(emitter, eventId, emitterId,
            "EventStream Created. [userId=" + email + "]");
        log.info("6");
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, email, emitterId, emitter);
        }
        log.info("7");
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

    public void send(CreateRequest createRequest) {
        String email = createRequest.getReceiver();
        Notification notification = notificationRepository.save(createNotification(
            createRequest.getReceiver(), createRequest.getSender(),
            createRequest.getUrl()));

        String eventId = email + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByAccountEmail(
            email);
        emitters.forEach(
            (key, emitter) -> {
                emitterRepository.saveEventCache(key, notification);
                sendNotification(emitter, eventId, key, notification);
            }
        );
    }


    private Notification createNotification(String receiver, String sender, String url) {
        return Notification.builder()
            .receiver(receiver)
            .sender(sender)
            .url(url)
            .isRead(false)
            .build();
    }
}