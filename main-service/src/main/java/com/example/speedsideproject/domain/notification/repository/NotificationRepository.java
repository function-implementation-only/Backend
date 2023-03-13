package com.example.speedsideproject.domain.notification.repository;

import com.example.speedsideproject.domain.applyment.entity.Applyment;
import com.example.speedsideproject.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByApplyment(Applyment applyment);

    List<Notification> findAllByReceiverAndIsReadFalse(String receiver);
}