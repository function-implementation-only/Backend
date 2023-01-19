package com.example.speedsideproject.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LoginReqDto {


    private String email;

    private String password;

    public LoginReqDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }
}

