package com.example.speedsideproject.applyment.dto;

import com.example.speedsideproject.applyment.Position;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ApplymentRequestDto {
    private Long postId;
    private Position position;
    private String comment;

}