package com.example.chatservice.constants;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConstants {
    public static final ResponseEntity<Void> OK =
        ResponseEntity.ok().build();

    public static final ResponseEntity<Void> CREATED =
        ResponseEntity.status(HttpStatus.CREATED).build();

    public static final ResponseEntity<Void> BAD_REQUEST =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    public static final ResponseEntity<String> NOT_LOGINED_USER =
        new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.NOT_FOUND);

    public static final ResponseEntity<String> NOT_FOUND_CHATROOM =
        new ResponseEntity<>("존재하지 않는 채팅방입니다.", HttpStatus.NOT_FOUND);

    public static final ResponseEntity<String> DUPLICATED_CHATROOM =
        new ResponseEntity<>("이미 존재하는 채팅방입니다.", HttpStatus.CONFLICT);
}
