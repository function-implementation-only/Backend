package com.example.speedsideproject.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.quarydsl.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByOrderByCreatedAtDesc();
//    List<Post> findAllByEmail(String email);
    Optional<Post> findById(Long Id);


    List<Post> findAllByAccount(Account account);

}
