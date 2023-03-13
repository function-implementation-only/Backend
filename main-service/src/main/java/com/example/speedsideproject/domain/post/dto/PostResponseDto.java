package com.example.speedsideproject.domain.post.dto;

import com.example.speedsideproject.domain.account.entity.Account;
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
public class PostResponseDto {

    private LocalDateTime createdAt;
    private Long postId;
    private String title;

    private Long accountId;
    private String email;
    private Category category;
    private Long duration;
    private Place place;
    private Long peopleNum;
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
    // 모집 인원
    private Long frontReqNum;
    private Long backReqNum;
    private Long designReqNum;
    private Long pmReqNum;
    private Long mobileReqNum;
    // 지원 인원
    private Long backendNum;
    private Long frontendNum;
    private Long designNum;
    private Long pmNum;
    private Long mobileNum;


    // content url
    private String contentUrl;
    private String contentKey;

    //조회수
    private Long viewCount;
    //서비스 분리
    @QueryProjection
    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
        this.accountId = post.getAccount().getId();
        this.profileImg = post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.techs = post.getTechs();
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.contentUrl = post.getContentUrl();
//        시간
        this.createdAt = post.createdAt;
//        this.frontReqNum = post.getFrontReqNum();
//        this.backReqNum = post.getBackReqNum();
//        this.designReqNum = post.getDesignReqNum();
        //조회수
        this.viewCount = post.getViewCount();
        //북마크 ...
        this.likeCheck = likeCheck;
    }


    //단일
    public PostResponseDto(Post post, Boolean likeCheck) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
        this.accountId = post.getAccount().getId();
        this.profileImg = post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.techs = post.getTechs();
        this.likeCheck = likeCheck;
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.contentUrl = post.getContentUrl();
        // 모집
        this.frontReqNum = post.getFrontReqNum();
        this.backReqNum = post.getBackReqNum();
        this.designReqNum = post.getDesignReqNum();
        this.pmReqNum = post.getPmReqNum();
        this.mobileReqNum = post.getMobileReqNum();
        // 지원
        this.backendNum = post.getBackendNum();
        this.frontendNum = post.getFrontendNum();
        this.designNum = post.getDesignNum();
        this.pmNum = post.getPmNum();
        this.mobileNum = post.getMobileNum();
        this.contentUrl = post.getContentUrl();
//        시간
        this.createdAt = post.createdAt;
        //조회수
        this.viewCount = post.getViewCount();
    }

    //단일
    public PostResponseDto(Post post, Boolean likeCheck, List<Long> countList) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.nickname = post.getAccount().getNickname();
        this.accountId = post.getAccount().getId();
        this.profileImg = post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.techs = post.getTechs();
        this.likeCheck = likeCheck;
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.contentUrl = post.getContentUrl();
        // count
        this.frontReqNum = post.getFrontReqNum();
        this.backReqNum = post.getBackReqNum();
        this.designReqNum = post.getDesignReqNum();
        this.pmReqNum = post.getPmReqNum();
        this.mobileReqNum = post.getMobileReqNum();

        this.backendNum = post.getBackendNum();
        this.frontendNum = post.getFrontendNum();
        this.designNum = post.getDesignNum();
        this.pmNum = post.getPmNum();
        this.mobileNum = post.getMobileNum();

        this.contentUrl = post.getContentUrl();
//        시간
        this.createdAt = post.createdAt;
        //조회수
        this.viewCount = post.getViewCount();
    }

    //단일 + no auth
    public PostResponseDto(Post post, List<Long> countList) {
        this.postId = post.getId();
        this.email = post.getAccount().getEmail();
        this.accountId = post.getAccount().getId();
        this.nickname = post.getAccount().getNickname();
        this.profileImg = post.getAccount().getImgUrl();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.techs = post.getTechs();
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
        this.contentUrl = post.getContentUrl();
        // count
        this.frontReqNum = post.getFrontReqNum();
        this.backReqNum = post.getBackReqNum();
        this.designReqNum = post.getDesignReqNum();
        this.pmReqNum = post.getPmReqNum();
        this.mobileReqNum = post.getMobileReqNum();

        this.backendNum = post.getBackendNum();
        this.frontendNum = post.getFrontendNum();
        this.designNum = post.getDesignNum();
        this.pmNum = post.getPmNum();
        this.mobileNum = post.getMobileNum();

        this.contentUrl = post.getContentUrl();
        //        시간
        this.createdAt = post.createdAt;
        //조회수
        this.viewCount = post.getViewCount();
    }

    public PostResponseDto(Post post, Account account) {
        this.postId = post.getId();
        this.accountId = post.getAccount().getId();
        this.title = post.getTitle();
        this.email = post.getAccount().getEmail();
        this.nickname = account.getNickname();
        this.profileImg = post.getAccount().getImgUrl();
        this.category = post.getCategory();
        this.duration = post.getDuration();
        this.place = post.getPlace();
        this.techs = post.getTechs();
        this.likesLength = post.getLikesLength();
        this.postState = post.getPostState();
//        시간
        this.createdAt = post.createdAt;
        //조회수
        this.viewCount = post.getViewCount();
    }


}