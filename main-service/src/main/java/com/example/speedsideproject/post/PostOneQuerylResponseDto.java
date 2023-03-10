package com.example.speedsideproject.post;

import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.PostState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Data
public class PostOneQuerylResponseDto {
    private Long postId;
    private LocalDateTime createdAt;
    private Techs techs;
    private String title;
    private Long viewCount;
    private Category category;
    private PostState postState;
    private Place place;
    private Long likesLength;
    private Long frontReqNum;
    private Long frontendNum;
    private Long backReqNum;
    private Long backendNum;
    private Long pmReqNum;
    private Long pmNum;
    private Long mobileReqNum;
    private Long mobileNum;
    private Long designReqNum;
    private Long designNum;
    private String contentUrl;
    private Long accountId;
    private String email;
    private String nickname;
    private String profileImg;
    private Boolean likeCheck;


    public PostOneQuerylResponseDto(
            Long postId,
            LocalDateTime createdAt,
            Techs techs,
            String title,
            Long viewCount,
            Category category,
            PostState postState,
            Place place,
            Long likesLength,
            Long frontReqNum,
            Long frontendNum,
            Long backReqNum,
            Long backendNum,
            Long pmReqNum,
            Long pmNum,
            Long mobileReqNum,
            Long mobileNum,
            Long designReqNum,
            Long designNum,
            String contentUrl,
            Long accountId,
            String email,
            String nickname,
            String profileImg,
            Boolean likeCheck) {
        this.postId = postId;
        this.createdAt = createdAt;
        this.techs=techs;
        this.title = title;
        this.viewCount = viewCount;
        this.category = category;
        this.postState = postState;
        this.place = place;
        this.likesLength = likesLength;
        this.frontReqNum = frontReqNum;
        this.frontendNum = frontendNum;
        this.backReqNum = backReqNum;
        this.backendNum = backendNum;
        this.pmReqNum = pmReqNum;
        this.pmNum = pmNum;
        this.mobileReqNum = mobileReqNum;
        this.mobileNum = mobileNum;
        this.designReqNum = designReqNum;
        this.designNum = designNum;
        this.contentUrl = contentUrl;
        this.accountId = accountId;
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.likeCheck = likeCheck;
    }
}
