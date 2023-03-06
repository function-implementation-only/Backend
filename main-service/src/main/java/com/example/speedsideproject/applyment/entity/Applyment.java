package com.example.speedsideproject.applyment.entity;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.applyment.Position;
import com.example.speedsideproject.applyment.dto.ApplymentRequestDto;
import com.example.speedsideproject.global.Timestamped;
import com.example.speedsideproject.post.Post;
import com.example.speedsideproject.sse.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Applyment extends Timestamped {

    @Id
    @Column(name = "applyment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(nullable = false)
    private String comment;


    // many applyment to one post.
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    // one applyment to one notification
    @OneToOne(mappedBy = "applyment",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    public Applyment(ApplymentRequestDto requestDto, Account account) {
        this.position = requestDto.getPosition();
        this.comment = requestDto.getComment();
        this.account = account;
    }

    //method
    public void setPost(Post post) {
        this.post = post;
        extracted(+1L);
    }

    public void update(ApplymentRequestDto requestDto) {
        deleteNum();
        this.position = requestDto.getPosition();
        this.comment = requestDto.getComment();
        extracted(+1L);
    }

    public void deleteNum() {
        extracted(-1L);
    }

    private void extracted(Long num) {
        if (position.equals(Position.BACKEND)) {
            this.post.setBackendNum(post.getBackendNum() + num);
            return;
        }
        if (position.equals(Position.FRONTEND)) {
            this.post.setFrontendNum(post.getFrontendNum() + num);
            return;
        }
        if (position.equals(Position.DESIGN)) {
            this.post.setDesignNum(post.getDesignNum() + num);
            return;
        }
        if (position.equals(Position.PM)) {
            this.post.setPmNum(post.getPmNum() + num);
            return;
        }
        if (position.equals(Position.MOBILE)) {
            this.post.setMobileNum(post.getMobileNum() + num);
        }
    }

}
