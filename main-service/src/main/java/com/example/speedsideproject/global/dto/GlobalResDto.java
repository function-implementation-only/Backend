package com.example.speedsideproject.global.dto;

import com.example.speedsideproject.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalResDto {

    private String msg;
    private int statusCode;

    public GlobalResDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

}
