package com.example.speedsideproject.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalResponseDto<T> {
    private Boolean success;
    private T data;
    private String msg;

    public static <T> GlobalResponseDto <T> success(T data,String msg){
        return new GlobalResponseDto<>(true, data, msg);
    }

    public static <T> GlobalResponseDto <T> fail(String msg){
        return new GlobalResponseDto<>(false, null, msg);
    }
}
