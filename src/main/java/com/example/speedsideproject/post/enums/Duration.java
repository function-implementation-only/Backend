package com.example.speedsideproject.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Duration {
    UNDEFINED("기간미정"), ONE("1개월"), TWO("2개월"), THREE("3개월"), FOUR("4개월"), FIVE("5개월"), SIX("6개월이상");
    private final String duration;
}
