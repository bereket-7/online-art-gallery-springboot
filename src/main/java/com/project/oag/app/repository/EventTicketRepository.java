package com.project.oag.app.repository;

import com.project.oag.app.entity.EventTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTicketRepository extends JpaRepository<EventTicket, Long> {
    List<EventTicket> findByUserId(Long userId);

    long countByEventId(Long eventId);
}
