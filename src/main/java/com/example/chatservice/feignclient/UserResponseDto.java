package com.example.chatservice.feignclient;

import lombok.Data;
import lombok.Getter;

@Data
public class UserResponseDto {
    public UserData data;
    @Data
    public static class UserData{
        public Long accountId;
        public String email;
        public String nickname;
        public String introduction;
        public String field;
        public String availableTime;
        @Getter public String imgUrl;
    }
}
