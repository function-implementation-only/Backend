package com.example.speedsideproject.post;


import com.example.speedsideproject.account.entity.Account;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private String email;
    private String urlToString;

    //null이라면 이 필드 값 ignore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getEmail();
        this.urlToString = post.getUrlToString();
        //this.nickname = post.
    }
    public PostResponseDto(Post post, Account account) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getEmail();
        this.urlToString = post.getUrlToString();
        this.nickname = account.getNickname();
    }


}