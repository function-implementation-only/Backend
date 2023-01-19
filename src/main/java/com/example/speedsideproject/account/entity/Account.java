package com.example.speedsideproject.account.entity;


import com.example.speedsideproject.account.dto.AccountReqDto;
import com.example.speedsideproject.account.dto.UserInfoDto;
import com.example.speedsideproject.applyment.entity.Applyment;
import com.example.speedsideproject.global.Timestamped;
import com.example.speedsideproject.post.Post;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor
public class Account extends Timestamped {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;
    @Column(nullable = false)
    private String nickname;
    private String imgUrl;
    private String imgKey;
    /*본인 개발 직군*/
    private String field = "";
    /*본인 소개글 */
    private String introduction = "";

    /* 연락 가능 시간 */
    private String availableTime = "";

    @Column
    private Boolean isAccepted = false;
    @Column
    private Boolean isDeleted;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Post> post;

    @OneToMany(mappedBy = "account")
    private List<Applyment> applymentList;

    @Builder
    public Account(String email, String nickname, String imgUrl, Boolean isAccepted, Boolean isDeleted) {
        this.email = email;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.isAccepted = isAccepted;
        this.isDeleted = isDeleted;
    }

    public Account(AccountReqDto accountReqDto) {
        this.email = accountReqDto.getEmail();
        this.password = accountReqDto.getPassword();
        this.nickname = accountReqDto.getNickname();
    }

    public void update(UserInfoDto userInfoDto, Map<String, String> urlMap) {
        this.nickname = (userInfoDto.getNickname() == null) ? this.getNickname() : userInfoDto.getNickname();
        this.field = (userInfoDto.getField() == null) ? this.getField() : userInfoDto.getField();
        this.introduction = (userInfoDto.getIntroduction() == null) ? this.getIntroduction() : userInfoDto.getIntroduction();
        this.availableTime = (userInfoDto.getAvailableTime() == null) ? this.getAvailableTime() : userInfoDto.getAvailableTime();
        this.imgUrl = urlMap.get("url");
        this.imgKey = urlMap.get("key");
    }

    public void update(UserInfoDto userInfoDto) {
        this.nickname = (userInfoDto.getNickname() == null) ? this.getNickname() : userInfoDto.getNickname();
        this.field = (userInfoDto.getField() == null) ? this.getField() : userInfoDto.getField();
        this.introduction = (userInfoDto.getIntroduction() == null) ? this.getIntroduction() : userInfoDto.getIntroduction();
        this.availableTime = (userInfoDto.getAvailableTime() == null) ? this.getAvailableTime() : userInfoDto.getAvailableTime();
    }

    /*비밀번호 재설정*/
    public void setPassword(String encode) {
        this.password = encode;
    }
}


