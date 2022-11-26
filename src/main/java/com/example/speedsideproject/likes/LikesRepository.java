package com.example.speedsideproject.likes;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByAccountAndPostId(Account account, Long postId);
    Optional<Likes> findByAccountAndPost(Account account, Post post);

}
