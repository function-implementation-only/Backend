package com.example.speedsideproject.domain.applyment.dto;


import com.example.speedsideproject.domain.applyment.Position;
import com.example.speedsideproject.domain.applyment.entity.Applyment;
import lombok.Getter;

@Getter
public class ApplymentResponseDto {
    private Long applymentId;
    private Long postId;
    private Position position;
    private String comment;


    //account 관련
    private Long accountId;
    private String nickname;
    private String introduction;
    private String imgUrl;

    public ApplymentResponseDto(Applyment applyment) {
        this.applymentId = applyment.getId();
        this.postId = applyment.getPost().getId();
        this.position = applyment.getPosition();
        this.accountId = applyment.getAccount().getId();
        this.nickname = applyment.getAccount().getNickname();
        this.introduction = applyment.getAccount().getIntroduction();
        this.comment = applyment.getComment();
        this.imgUrl = applyment.getAccount().getImgUrl();
    }

    public ApplymentResponseDto(Applyment applyment, Long postId) {
        this.applymentId = applyment.getId();
        this.postId = postId;
        this.position = applyment.getPosition();
        this.accountId = applyment.getAccount().getId();
        this.nickname = applyment.getAccount().getNickname();
        this.introduction = applyment.getAccount().getIntroduction();
        this.comment = applyment.getComment();
        this.imgUrl = applyment.getAccount().getImgUrl();
    }


}