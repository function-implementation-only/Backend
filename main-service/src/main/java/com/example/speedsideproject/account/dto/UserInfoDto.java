package com.example.speedsideproject.account.dto;

import com.example.speedsideproject.account.entity.Account;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@AllArgsConstructor
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDto {

    private Long accountId;
    private String email;
    private String nickname="";
    private String introduction="";
    private String field="";

    private String imgUrl;

    private String availableTime="";



@Builder
    public UserInfoDto(Account account) {
        this.accountId =account.getId();
        this.email = account.getEmail();
        this.imgUrl = account.getImgUrl();
        this.availableTime = account.getAvailableTime();
        this.nickname = account.getNickname();
        this.introduction = account.getIntroduction();
        this.field = account.getField();
    }
}
