package com.example.speedsideproject.sse;

import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.error.ErrorCode;
import com.example.speedsideproject.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 1000L * 60 * 60;

    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long accountId, String lastEventId) {
        String id = accountId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(id,new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        sendToClient(emitter, id, "EventStream Created. [accountId" + accountId + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(accountId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(eventId);
        }
    }

    /*@Async
    public void send(Account receiver, AlarmType alarmType, String message, Long articlesId, String title) {
        Notification notification = notificationRepository.save(createNotification(receiver, alarmType, message, articlesId, title));

        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    NotificationResponseDto notificationresponseDto = new NotificationResponseDto(notification);
                    sendToClient(emitter, eventId, notificationresponseDto);
                }
        );
    }*/

    private Object createNotification(Account receiver, AlarmType alarmType, String message,
                                      Long articlesId, String title) {
        return Notification.builder()
                .receiver(receiver)
                .alarmType(alarmType)
                .message(message)
                .articlesId(articlesId)
                .title(title)
                .readState(false) // 현재 읽음상태
                .build();
    }

    @Transactional
    public ResponseDto getAllNotification(Account account) {
        List<Notification> notificationList = notificationRepository.findAllByMemberOrderByIdDesc(account);
        List<NotificationResponseDto> notificationResponseDtoList = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationResponseDto notificationResponseDto = new NotificationResponseDto(notification);
            notificationResponseDtoList.add(notificationResponseDto);
        }
        return ResponseDto.success(notificationResponseDtoList);
    }

    public String delectNotification(Account account, Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        if (!notification.getAccount().getId().equals(account.getId())) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        notificationRepository.delete(notification);
        return "삭제 성공";
    }

    @Transactional
    public String readNotification(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        notification.changeState();
        notificationRepository.save(notification);
        return "읽음 완료";
    }


    public Long notReadNotification(Account account) {
        return notificationRepository.countUnReadStateNotifications(account.getId());
    }
}
