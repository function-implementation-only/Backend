package com.example.speedsideproject.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.quarydsl.post.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByOrderByCreatedAtDesc();

    //    List<Post> findAllByEmail(String email);
    Optional<Post> findById(Long Id);


    Post findByIdAndAccount(Long id, Account account);

//    List<Post> findAllByAccount(Account account);

    List<Post> findTop5ByAccountOrderByIdDesc(Account account);

}
