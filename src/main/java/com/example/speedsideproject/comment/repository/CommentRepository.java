package com.example.speedsideproject.comment.repository;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByCreatedAtDesc();

    List<Comment> findAllByAccount(Account account);

    List<Comment> findAllByEmail(String email);

}