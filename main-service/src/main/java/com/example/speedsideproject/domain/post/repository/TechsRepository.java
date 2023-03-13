package com.example.speedsideproject.domain.post.repository;

import com.example.speedsideproject.domain.post.entity.Techs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechsRepository extends JpaRepository<Techs,Long> {
    List<Techs> findAllByPostId(Long id);

}
