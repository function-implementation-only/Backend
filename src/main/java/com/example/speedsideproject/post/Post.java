package com.example.speedsideproject.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.comment.entity.Comment;
import com.example.speedsideproject.global.Timestamped;
import com.example.speedsideproject.likes.Likes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Entity
public class Post extends Timestamped {

    @Id
    @Column(name = "POST_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private String title;
    @Column(nullable = true)
    private String contents;
    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String urlToString;

    @JsonIgnore
    @Column(nullable = true)
    private String urlKey;

    @JsonIgnore //JPA 순환참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_Id")
    private Account account;

    //One post to Many comment
    @OneToMany(mappedBy = "post")
    private List<Comment> comment;

    // 연관관계
    // One Post To Many Likes
    @OneToMany(mappedBy = "post")
    private List<Likes> likes;

    @Column(nullable = true)
    private Long likesLength = 0L;

    @Enumerated(EnumType.STRING)
    private PostType postType;


    public Post(String contents, String title) {
        this.contents = contents;
        this.title = title;
    }

    public Post(PostRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
    }

    public Post(Post post) {
        this.contents = post.getContents();
        this.title = post.getTitle();
        this.account = post.getAccount();
        this.email = post.getEmail();
        this.urlToString = post.getUrlToString();
        this.urlKey = post.getUrlKey();
    }

    public Post(PostRequestDto requestDto, Account account, Map<String, String> urlMap) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
        this.account = account;
        this.email = account.getEmail();
        this.urlToString = urlMap.get("url");
        this.urlKey = urlMap.get("key");
        this.postType = requestDto.getType();
    }

    public Post(PostRequestDto requestDto, Account account) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
        this.account = account;
        this.email = account.getEmail();
        this.postType = requestDto.getType();
    }

    public Post(PostRequestDto requestDto, Map<String, String> urlMap) {
        this.contents = requestDto.getContents();
        // this.title = requestDto.getTitle();
        this.urlToString = urlMap.get("url");
        this.urlKey = urlMap.get("key");
    }

    //글내용만 업데이트
    public void update(PostRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
    }

    //라이크의 갯수를 추가하는 메소드
    public void setLikesLength(boolean likesType) {
        this.likesLength = (likesType) ? this.likesLength + 1L : this.likesLength - 1L;
    }
}

