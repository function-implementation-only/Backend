package com.example.speedsideproject.domain.post.dto;

import com.example.speedsideproject.domain.post.entity.Post;
import com.example.speedsideproject.domain.post.entity.Techs;
import com.example.speedsideproject.domain.post.enums.Place;
import com.example.speedsideproject.domain.post.enums.Category;
import com.example.speedsideproject.domain.post.enums.PostState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PostQueryResponseDto {

    private LocalDateTime createdAt;
    private Long postId;
    private String title;

    private Long accountId;
    private String email;
    private Category category;
//    private Long duration;
    private Place place;
    private PostState postState;
    //Techs 안에 tech가 있다...
    //이 부분을 염두하고 코드 수정 바랍니다
    private List<Techs> techs;
    //private List<Image> imageList;
    private Long likesLength;
    private Boolean likeCheck;
    //null이라면 이 필드 값 ignore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    private String profileImg;


    // content url
    private String contentUrl;

    //조회수
    private Long viewCount;

    //서비스 분리
    @QueryProjection
    public PostQueryResponseDto(Post post, Boolean likeCheck) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
        this.accountId = post.getAccount().getId();
        this.profileImg = post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
//        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.techs = post.getTechs();
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.contentUrl = post.getContentUrl();
//        시간
        this.createdAt = post.createdAt;
        //조회수
        this.viewCount = post.getViewCount();
        //북마크 ...
        this.likeCheck = likeCheck;
    }

}