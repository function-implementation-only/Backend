package com.example.chatservice.global.exception;


import static com.example.chatservice.global.exception.constants.ResponseConstants.DUPLICATED_CHATROOM;
import static com.example.chatservice.global.exception.constants.ResponseConstants.NOT_FOUND_CHATROOM;

import com.example.chatservice.global.exception.chatroom.ChatRoomNotFoundException;
import com.example.chatservice.global.exception.chatroom.DuplicatedChatRoomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {


    @ExceptionHandler(ChatRoomNotFoundException.class)
    public final ResponseEntity<String> handleChatRoomNotFoundException(
        ChatRoomNotFoundException exception) {
        log.debug("존재하지 않는 채팅방입니다..", exception);
        return NOT_FOUND_CHATROOM;
    }

    @ExceptionHandler(DuplicatedChatRoomException.class)
    public final ResponseEntity<String> handleDuplicatedChatRoomException(
        DuplicatedChatRoomException exception) {
        log.debug("이미 존재하는 채팅방입니다", exception);
        return DUPLICATED_CHATROOM;
    }
}

