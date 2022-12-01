package com.example.speedsideproject.account.entity;


import com.example.speedsideproject.account.dto.AccountReqDto;
import com.example.speedsideproject.post.Post;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import com.example.speedsideproject.account.dto.UserInfoDto;
import com.example.speedsideproject.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    private String imgUrl;
    private String imgKey;
    @Column
    private Boolean isAccepted = false;
    @Column
    private Boolean isDeleted;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Post> post;

    @OneToMany(mappedBy = "account")
    private List<Comment> commentList;

    @Builder
    public Account(String email, String nickname, String imgUrl, Boolean isAccepted, Boolean isDeleted){
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
        this.nickname =
                (userInfoDto.getNickname().isBlank()) ? this.getNickname() : userInfoDto.getNickname();
        this.imgUrl = urlMap.get("url");
        this.imgKey = urlMap.get("key");
    }

    public void update(UserInfoDto userInfoDto) {
        this.nickname = userInfoDto.getNickname();
    }

}

