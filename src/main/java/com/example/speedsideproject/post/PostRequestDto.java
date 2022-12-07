package com.example.speedsideproject.post;

import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Duration;
import com.example.speedsideproject.post.enums.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String startDate;
}