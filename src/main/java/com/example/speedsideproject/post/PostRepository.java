package com.example.speedsideproject.post;


import com.example.speedsideproject.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
//    List<Post> findAllByEmail(String email);
    Optional<Post> findById(Long Id);
    Post findByIdAndAccount(Long id, Account account);
    List<Post> findAllByAccount(Account account);

}
