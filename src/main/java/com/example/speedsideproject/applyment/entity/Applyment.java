package com.example.speedsideproject.applyment.entity;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.applyment.Position;
import com.example.speedsideproject.applyment.dto.ApplymentRequestDto;
import com.example.speedsideproject.global.Timestamped;
import com.example.speedsideproject.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
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


    // many applyment to one post.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public Applyment(ApplymentRequestDto requestDto) {
        this.position = requestDto.getPosition();
    }

    public Applyment(ApplymentRequestDto requestDto, Account account) {
        this.position = requestDto.getPosition();
//        this.post = requestDto.getPostId();
        this.account = account;
    }

    public void update(ApplymentRequestDto requestDto) {
        this.position = requestDto.getPosition();
    }

}
