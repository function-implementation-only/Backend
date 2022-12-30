package com.example.speedsideproject.websocket.controller;



import com.example.speedsideproject.global.dto.GlobalResponseDto;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import com.example.speedsideproject.websocket.dto.RoomReqDto;
import com.example.speedsideproject.websocket.service.ChatService;
import com.example.speedsideproject.websocket.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j

public class RoomController {
    private final ChatService chatService;
    private final RoomService roomService;
    @GetMapping("/roomInfo/{roaaomId}")
    public GlobalResponseDto<?> joinRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("chatList", chatList);
        return roomService.joinRoom(roomId, userDetails);
    }

    /**
     * 채팅방 등록
     */
    @PostMapping("/room")
    public GlobalResponseDto<?> createRoom(@RequestBody RoomReqDto roomReqDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(roomReqDto.getPostId().toString());
        return roomService.createRoom(roomReqDto, userDetails);
    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/roomList")
    public GlobalResponseDto<?> roomList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.roomList(userDetails);
    }
}
