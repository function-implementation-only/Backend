package com.example.chatservice.chat.controller;

import com.example.chatservice.global.security.user.UserDetailsImpl;
import com.example.chatservice.chat.dto.ChatDto.CreateResponse;
import com.example.chatservice.chat.dto.ChatRoomDto.CreateRequest;
import com.example.chatservice.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.chatservice.chat.dto.ChatRoomDto.Response;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**채팅방 생성 메서드 */
    @PostMapping
    public ResponseEntity<CreateResponse> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody CreateRequest request) {
        log.info(userDetails.getAccount());
        return ResponseEntity.ok(chatRoomService.createRoom(request, userDetails));
    }

    /** 채팅방 삭제 메서드 */
    @PostMapping("/{roomName}")
    public ResponseEntity<Void> deleteBoard(@PathVariable String roomName,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomService.deleteRoom(roomName, userDetails);
        return ResponseEntity.ok().build();
    }

    /** 채팅방 상세 조회 메서드 */
    @GetMapping("/{roomName}")
    public ResponseEntity<Response> getChatRoomDetail(@PathVariable String roomName,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
            chatRoomService.getChatRoomDetail(roomName, userDetails.getAccount()));
    }

    /** 내가 참여중인 채팅방 리스트 */
    @GetMapping("/list")
    public ResponseEntity<Page<Response>> getChatRoomList(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PageableDefault Pageable pageable) {

        return ResponseEntity.ok(
            chatRoomService.getChatRoomList(userDetails.getAccount(), pageable));
    }


}
