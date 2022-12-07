package com.example.speedsideproject.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Duration;
import com.example.speedsideproject.post.enums.Place;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private String email;
    private Category category;
    private Duration duration;
    private Place place;
    private Long peopleNum;

    //Techs 안에 tech가 있다...
    //이 부분을 염두하고 코드 수정 바랍니다
    private List<Techs> techs;
    private List<Image> imageList;
    private String startDate;
    private Long likesLength;
    private Boolean likeCheck;
    //null이라면 이 필드 값 ignore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;

    private String profileImg;
    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
        this.profileImg=post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.imageList = post.getImageList();
        this.contents = post.getContents();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
        this.techs = post.getTechs();
        this.startDate = post.getStartDate();
        this.likesLength = post.getLikesLength();
    }

    public PostResponseDto(Post post, Account account) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getAccount().getEmail();
        this.nickname = account.getNickname();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
//        this.tech = post.getTech();
        this.startDate = post.getStartDate();
        this.likesLength = post.getLikesLength();
    }
}