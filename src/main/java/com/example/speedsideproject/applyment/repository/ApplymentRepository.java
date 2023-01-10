package com.example.speedsideproject.applyment.repository;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.applyment.Position;
import com.example.speedsideproject.applyment.entity.Applyment;
import com.example.speedsideproject.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApplymentRepository extends JpaRepository<Applyment, Long> {
    List<Applyment> findAllByOrderByCreatedAtDesc();

    List<Applyment> findAllByAccount(Account account);
    //포지션별 갯수

    Long countByPostAndPosition(Post post, Position position);


}