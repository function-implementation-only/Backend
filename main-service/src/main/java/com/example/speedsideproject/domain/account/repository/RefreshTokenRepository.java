package com.example.speedsideproject.domain.account.repository;


import com.example.speedsideproject.domain.account.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByAccountEmail(String email);
    //RefreshToken findByAccountEmail(String email);
}
