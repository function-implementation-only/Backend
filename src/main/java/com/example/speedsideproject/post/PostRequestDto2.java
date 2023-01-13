package com.example.speedsideproject.post;

import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.PostState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
class PostRequestDto2 {
    private String title;
    private Category category;
    private Long duration;
    private Long frontReqNum=0L;
    private Long backReqNum=0L;
    private Long designReqNum=0L;
    private Long pmReqNum=0L;
    private Long mobileReqNum=0L;
    private Place place;
    private String startDate;
    private PostState postState;
}