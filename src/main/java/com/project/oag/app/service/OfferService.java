package com.project.oag.app.service;

import com.project.oag.app.dto.OfferStatus;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Offer;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.OfferRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public OfferService(OfferRepository offerRepository,
                        ArtworkRepository artworkRepository,
                        UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Offer makeOffer(Long artworkId, Long buyerId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GeneralException("Offer amount must be positive");
        }
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));

        Offer offer = new Offer();
        offer.setArtwork(artwork);
        offer.setBuyer(buyer);
        offer.setAmount(amount);
        offer.setStatus(OfferStatus.PENDING);
        return offerRepository.save(offer);
    }

    @Transactional
    public Offer updateOfferStatus(Long offerId, OfferStatus status) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        offer.setStatus(status);
        return offerRepository.save(offer);
    }

    public List<Offer> getOffersForArtwork(Long artworkId) {
        return offerRepository.findByArtworkId(artworkId);
    }

    public List<Offer> getOffersByBuyer(Long buyerId) {
        return offerRepository.findByBuyerId(buyerId);
    }
}
