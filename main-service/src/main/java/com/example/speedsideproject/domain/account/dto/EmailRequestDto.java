package com.example.speedsideproject.domain.account.dto;

import lombok.Data;

import javax.validation.constraints.Email;
@Data
public class EmailRequestDto {

    @Email
    private String email;

}
