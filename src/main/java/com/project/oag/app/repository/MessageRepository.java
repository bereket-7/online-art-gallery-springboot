package com.project.oag.app.repository;

import com.project.oag.app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByThreadIdOrderByCreatedAtAsc(Long threadId);

    Optional<Message> findTopByThreadIdOrderByCreatedAtDesc(Long threadId);

    long countByThreadIdAndReadFalseAndSenderIdNot(Long threadId, Long senderId);
}
