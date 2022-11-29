package com.example.speedsideproject.account.dto;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;


@NoArgsConstructor
@Getter
public class UserInfoDto {

    private String email;
    private String nickname;

    public UserInfoDto(Account account) {
        this.email = account.getEmail();
        this.nickname = account.getNickname();
    }

}
