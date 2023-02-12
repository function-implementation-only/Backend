package com.example.exception;


import static com.example.chatservice.constants.ResponseConstants.NOT_FOUND_CHATROOM;

import com.example.exception.chatroom.ChatRoomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

}

