package com.example.speedsideproject.account.entity;


import com.example.speedsideproject.account.dto.AccountReqDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    private String imgUrl;
    private String imgKey;
    @Column
    private Boolean isAccepted = false;
    @Column
    private Boolean isDeleted;

    @Builder
    public Account(String email, String nickname, String imgUrl, Boolean isAccepted, Boolean isDeleted){
        this.email = email;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.isAccepted = isAccepted;
        this.isDeleted = isDeleted;
    }

    public Account(AccountReqDto accountReqDto) {
        this.email = accountReqDto.getEmail();
        this.password = accountReqDto.getPassword();
        this.nickname = accountReqDto.getNickname();
    }

    public void update(AccountReqDto accountReqDto, Map<String,String> urlMap) {
        this.email = accountReqDto.getEmail();
        this.password = accountReqDto.getPassword();
        this.nickname = accountReqDto.getNickname();
        this.imgUrl = urlMap.get("url");
        this.imgKey = urlMap.get("key");
    }
}

