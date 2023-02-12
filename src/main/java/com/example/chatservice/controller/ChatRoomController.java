package com.example.chatservice.controller;

import com.example.chatservice.config.security.user.UserDetailsImpl;
import com.example.chatservice.dto.ChatRoomDto;
import com.example.chatservice.dto.ChatRoomDto.CreateRequest;
import com.example.chatservice.service.ChatRoomService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
    * 채팅방 생성 메서드
     */
    @PostMapping
    public ResponseEntity<String> createRoom(@AuthenticationPrincipal  UserDetailsImpl userDetails,
            @Valid @RequestBody CreateRequest request){
            log.info(userDetails.getAccount());
        return ResponseEntity.ok(chatRoomService.createRoom(request, userDetails));
    }

    /**
     * 채팅방 삭제 메서드
     */
    @PostMapping("/{roomName}")
    public ResponseEntity<Void> deleteBoard(@PathVariable String roomName,
        @AuthenticationPrincipal  UserDetailsImpl userDetails) {
        chatRoomService.deleteRoom(roomName, userDetails);
        return ResponseEntity.ok().build();
    }

    /**
     * 채팅방 상세 조회 메서드
     */
    @GetMapping("/{roomName}")
    public ResponseEntity<ChatRoomDto.Response> getChatRoomDetail(@PathVariable String roomName,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(chatRoomService.getChatRoomDetail(roomName, userDetails.getAccount()));
    }

    /**
     * 내가 참여중인 채팅방 리스트
     */
//    @GetMapping("/list")
//    public ResponseEntity<ChatRoomDto.Response> getChatRoomList(
//        @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return ReponseEntity
//    }




}
