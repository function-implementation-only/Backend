package com.example.chatservice.repository;

import com.example.chatservice.model.ChatRoom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findChatRoomByRoomName(String roomName);

    @Query(value = "SELECT r FROM ChatRoom r WHERE r.sender = :email OR r.receiver = :email " +
        "ORDER BY r.updatedAt DESC ",
        countQuery = "SELECT COUNT(r) FROM ChatRoom r " +
            "WHERE r.sender = :email " +
            "OR r.receiver = :email"
    )
    Page<ChatRoom> findAllByEmail(@Param("email") String email, Pageable pageable);


    @Query("SELECT COUNT(r) > 0 FROM ChatRoom r "
        + "WHERE (r.receiver = :receiver AND r.sender = :sender) "
        + "OR r.sender = :receiver AND r.receiver = :sender")
    boolean existsByReceiverAndSender(@Param("receiver") String receiver,
        @Param("sender") String sender);

    @Query("SELECT r FROM ChatRoom r " + "WHERE (r.receiver = :receiver AND r.sender = :sender) "
        + "OR r.sender = :receiver AND r.receiver = :sender")
    ChatRoom findByReceiverAndSender(@Param("receiver") String receiver,
        @Param("sender") String sender);
}
