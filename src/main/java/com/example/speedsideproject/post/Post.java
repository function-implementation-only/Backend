package com.example.speedsideproject.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.comment.entity.Comment;
import com.example.speedsideproject.global.Timestamped;
import com.example.speedsideproject.likes.Likes;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.PostState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Post extends Timestamped {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private String title;

    @Column(nullable = true)
    private String contents;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = true)
    private Long duration;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Place place;

    private Long peopleNum;

    @Column(nullable = true)
    private String startDate;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_Id")
    private Account account;

    //One post to Many comment
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comment;

    // One Post To Many Likes
    //cascade 어떻게 할것인지.....
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Likes> likes;

    @Column(nullable = true)
    private Long likesLength = 0L;

    //one post to many images
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Image> imageList = new ArrayList<>();

    //one post to many tech
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Techs> techs = new ArrayList<>();

    //모집중 or 모집완료
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private PostState postState;

    public Post(PostRequestDto requestDto, Account account) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
        this.account = account;
        this.category = requestDto.getCategory();
        this.duration = requestDto.getDuration();
        this.peopleNum = requestDto.getPeopleNum();
        this.place = requestDto.getPlace();
        this.startDate = requestDto.getStartDate();
        this.postState = requestDto.getPostState();
    }

    //method
    //글내용만 업데이트
    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.category = requestDto.getCategory();
        this.duration = requestDto.getDuration();
        this.peopleNum = requestDto.getPeopleNum();
        this.place = requestDto.getPlace();
        this.startDate = requestDto.getStartDate();
        this.postState = requestDto.getPostState();
    }

    //라이크의 갯수를 추가하는 메소드
    public void setLikesLength(boolean likesType) {
        this.likesLength = (likesType) ? this.likesLength + 1L : this.likesLength - 1L;
    }

    //연관관계 맵핑
    public void addImg(Image image) {
        this.imageList.add(image);
        image.setPost(this);
    }

    public void addTechs(Techs techs) {
        this.techs.add(techs);
    }
}

