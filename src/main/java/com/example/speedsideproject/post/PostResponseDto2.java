package com.example.speedsideproject.post;

import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.PostState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto2 {
    private Long postId;
    private String title;

    private String email;
    private Category category;
    private Long duration;
    private Place place;
    private Long peopleNum;
    private PostState postState;
    //Techs 안에 tech가 있다...
    //이 부분을 염두하고 코드 수정 바랍니다
    private List<Techs> techs;
//    private List<Image> imageList;
    private String startDate;
    private Long likesLength;
    private Boolean likeCheck;
    //null이라면 이 필드 값 ignore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    private String profileImg;

    //count
    private Long backendNum;
    private Long frontendNum;
    private Long designNum;

    // content url
    private String contentUrl;
    private String contentKey;


    //서비스 분리
    @QueryProjection
    public PostResponseDto2(Post post) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
//       this.profileImg=post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
        this.techs = post.getTechs();
        this.startDate = post.getStartDate();
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.contentUrl = post.getContentUrl();
        this.contentKey = post.getContentKey();
    }

    //단일
    public PostResponseDto2(Post post, Boolean likeCheck) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
        this.profileImg = post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
        this.techs = post.getTechs();
        this.startDate = post.getStartDate();
        this.likeCheck = likeCheck;
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.contentUrl = post.getContentUrl();
        this.contentKey = post.getContentKey();
    }

    //단일
    public PostResponseDto2(Post post, Boolean likeCheck, List<Long> countList) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
        this.profileImg = post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
        this.techs = post.getTechs();
        this.startDate = post.getStartDate();
        this.likeCheck = likeCheck;
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.backendNum = countList.get(0);
        this.frontendNum = countList.get(1);
        this.designNum = countList.get(2);
        this.contentUrl = post.getContentUrl();
        this.contentKey = post.getContentKey();
    }

    //단일 + no auth
    public PostResponseDto2(Post post, List<Long> countList) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
        this.profileImg = post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
        this.techs = post.getTechs();
        this.startDate = post.getStartDate();
//        this.likeCheck = likeCheck;
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.backendNum = countList.get(0);
        this.frontendNum = countList.get(1);
        this.designNum = countList.get(2);
        this.contentUrl = post.getContentUrl();
        this.contentKey = post.getContentKey();
    }

    public PostResponseDto2(Post post, Account account) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.email = post.getAccount().getEmail();
        this.nickname = account.getNickname();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.peopleNum = post.getPeopleNum();
//        this.tech = post.getTech();
        this.startDate = post.getStartDate();
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
    }


}