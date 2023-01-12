package com.example.speedsideproject.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechsRepository extends JpaRepository<Techs,Long> {
    List<Techs> findAllByPostId(Long id);

}
