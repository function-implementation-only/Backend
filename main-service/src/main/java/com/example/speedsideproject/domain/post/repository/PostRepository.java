package com.example.speedsideproject.domain.post.repository;


import com.example.speedsideproject.domain.account.entity.Account;
import com.example.speedsideproject.domain.post.entity.Post;
import com.example.speedsideproject.domain.post.quarydsl.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByOrderByCreatedAtDesc();

    Optional<Post> findById(Long Id);

    Post findByIdAndAccount(Long id, Account account);

    List<Post> findTop5ByAccountOrderByIdDesc(Account account);

}
