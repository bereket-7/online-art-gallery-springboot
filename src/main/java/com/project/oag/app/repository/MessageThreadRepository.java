package com.project.oag.app.repository;

import com.project.oag.app.entity.MessageThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageThreadRepository extends JpaRepository<MessageThread, Long> {
    List<MessageThread> findByBuyerIdOrArtistIdOrderByCreationDateDesc(Long buyerId, Long artistId);

    Optional<MessageThread> findByBuyerIdAndArtistIdAndArtworkId(Long buyerId, Long artistId, Long artworkId);
}
