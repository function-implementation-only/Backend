package com.example.speedsideproject.domain.likes.repository;


import com.example.speedsideproject.domain.account.entity.Account;
import com.example.speedsideproject.domain.likes.Likes;
import com.example.speedsideproject.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByAccountAndPostId(Account account, Long postId);
    Optional<Likes> findByAccountAndPost(Account account, Post post);
    boolean existsByAccountAndPost(Account account, Post post);
    List<Likes> findLikesByAccount(Account account);
}
