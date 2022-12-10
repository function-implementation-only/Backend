package com.example.speedsideproject.sse;

import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last_Event_ID",required = false,defaultValue = "")
                                String lastEventId) {
        return notificationService.subscribe(userDetails.getAccount().getId(), lastEventId);
    }

    //리스트 전체 가져오기
    @ApiOperation(value = "전체 알림 가져오기", notes = "토큰이 필요합니다. 지금까지 온 모든 알림을 조회합니다.")
    @GetMapping("/getnotice")
    public ResponseDto<?> getAllNotification(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.getAllNotification(userDetails.getAccount());
    }

    //알림삭제
    @ApiOperation(value = "알림 삭제", notes = "지금까지 온 알림 중에 선택한 알맇을 삭제합니다.(토크필요)")
    @DeleteMapping("/notice/{id}")
    public ResponseDto deleteNotification(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable Long id) {
        return ResponseDto.success(notificationService.delectNotification(userDetails.getAccount(), id));
    }

    //알림 읽음
    @ApiOperation(value = "알림 읽음", notes = "선택한 알림의 상태를 true로 만듭니다.")
    @PutMapping("/notice/{id}")
    public ResponseDto<?> readNotification(@PathVariable Long id) {
        return ResponseDto.success(notificationService.readNotification(id));
    }

    //안읽은 알림갯수
    @ApiOperation(value = "안읽은 알림갯수", notes = "상태가 false인 알림의 갯수를 반환합니다.(토큰필요)")
    @GetMapping("nureadnotice")
    public Long notReadNotification(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.notReadNotification(userDetails.getAccount());
    }



}
