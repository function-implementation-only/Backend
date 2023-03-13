package com.example.chatservice.tests.repository;

import com.example.chatservice.tests.entity.Testing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestingRepository extends JpaRepository<Testing,Long> {
}
