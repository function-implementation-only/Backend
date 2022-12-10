package com.example.speedsideproject.sse;

import com.example.speedsideproject.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByMemberOrderByIdDesc(Account account);

    Long countUnReadStateNotifications(Long id);
}
