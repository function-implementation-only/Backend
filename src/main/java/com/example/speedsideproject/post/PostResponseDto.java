package com.example.speedsideproject.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Duration;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.Tech;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private String email;
    private String urlToString;
    private Category category;
    private Duration duration;
    private Place place;
    private Long peopleNum;
    private Tech tech;
    private String startDate;
    private Long likesLength;
    private Boolean likeCheck;
    //null이라면 이 필드 값 ignore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getAccount().getEmail();
        this.urlToString = post.getUrlToString();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
        this.tech = post.getTech();
        this.startDate = post.getStartDate();
        this.likesLength = post.getLikesLength();
    }

    public PostResponseDto(Post post, Account account) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getAccount().getEmail();
        this.urlToString = post.getUrlToString();
        this.nickname = account.getNickname();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
        this.tech = post.getTech();
        this.startDate = post.getStartDate();
        this.likesLength = post.getLikesLength();
    }
}