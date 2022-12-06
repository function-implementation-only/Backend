package com.example.speedsideproject.post;

import com.example.speedsideproject.post.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private String title;
    private String contents;
    private Category category;
    private Duration duration;
    private Long peopleNum;
    private Place place;
    private Tech tech;
    private String startDate;
}