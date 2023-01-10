package com.example.speedsideproject.account.dto;

import com.example.speedsideproject.account.entity.Account;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class UserInfoDto {

    private String nickname="";
    private String introduction="";
    private String field="";

    @Builder
    public UserInfoDto(Account account) {
        this.nickname = account.getNickname();
        this.introduction = account.getIntroduction();
        this.field = account.getField();
    }
}
