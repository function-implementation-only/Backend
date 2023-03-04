package com.example.speedsideproject.likes;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.global.Timestamped;
import com.example.speedsideproject.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Likes extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @Column(nullable = true)
    private Boolean likeCheck;

    //연관관계
    // Many Likes To One Post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    //연관관계
    // Many Likes To One Account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = true)
    private Account account;

    public Likes(Account account, Post post) {
        this.account = account;
        this.post = post;
        this.likeCheck = true;
    }

    public Boolean getLikeCheck() {
        return this.likeCheck;
    }

    public Boolean setLikeCheck(Boolean likeCheck) {
        this.likeCheck = likeCheck;
        return this.likeCheck;
    }

}
