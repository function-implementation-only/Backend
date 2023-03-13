package com.example.speedsideproject.domain.post.dto;

import com.example.speedsideproject.domain.post.enums.Place;
import com.example.speedsideproject.domain.post.enums.Category;
import com.example.speedsideproject.domain.post.enums.PostState;
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
    private Category category;
    private Long duration;
    private Long frontReqNum=0L;
    private Long backReqNum=0L;
    private Long designReqNum=0L;
    private Long pmReqNum=0L;
    private Long mobileReqNum=0L;
    private Place place;
    private PostState postState;
}