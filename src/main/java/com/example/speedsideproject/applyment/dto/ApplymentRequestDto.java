package com.example.speedsideproject.applyment.dto;

import com.example.speedsideproject.applyment.Position;
import lombok.Getter;

@Getter
public class ApplymentRequestDto {
    private Long postId;
    private Position position;
}