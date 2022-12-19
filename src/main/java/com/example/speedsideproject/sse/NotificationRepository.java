package com.example.speedsideproject.sse;

import com.example.speedsideproject.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select n from Notification n where n.account.id = :id order by n.id desc")
    List<Notification> findAllByReceiver_AccountId(@Param("id") Long id);

    @Query(value = "select count(n) from Notification n where n.account.id =:id and n.readState = false")
    Long countUnReadStateNotifications(@Param("id") Long id);

    List<Notification> findAllByAccountOrderByIdDesc(Account account);
}
