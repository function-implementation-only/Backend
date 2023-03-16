package com.example.speedsideproject.domain.applyment.repository;


import com.example.speedsideproject.domain.account.entity.Account;
import com.example.speedsideproject.domain.applyment.Position;
import com.example.speedsideproject.domain.applyment.entity.Applyment;
import com.example.speedsideproject.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApplymentRepository extends JpaRepository<Applyment, Long> {
    List<Applyment> findAllByOrderByCreatedAtDesc();

    List<Applyment> findAllByAccount(Account account);
//    List<Applyment> findTop5ByAccountOrderByIdDesc(Account account);

    //포지션별 갯수
    Long countByPostAndPosition(Post post, Position position);

   Boolean existsByAccountAndPost(Account account, Post post);


}