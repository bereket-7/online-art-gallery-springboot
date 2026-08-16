package com.project.oag.app.repository;

import com.project.oag.app.dto.OfferStatus;
import com.project.oag.app.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByArtworkId(Long artworkId);

    List<Offer> findByBuyerId(Long buyerId);

    List<Offer> findByStatus(OfferStatus status);

    List<Offer> findByArtworkUserIdAndStatus(Long artistId, OfferStatus status);
}
