package com.project.oag.app.service;

import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.dto.OfferAcceptResponseDto;
import com.project.oag.app.dto.OfferResponseDto;
import com.project.oag.app.dto.OfferStatus;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Offer;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.OfferRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.BadRequestException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public OfferService(OfferRepository offerRepository,
                        ArtworkRepository artworkRepository,
                        UserRepository userRepository,
                        OrderService orderService) {
        this.offerRepository = offerRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @Transactional
    public OfferResponseDto makeOffer(Long artworkId, Long buyerId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Offer amount must be positive");
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
        return CommerceMappers.toOfferDto(offerRepository.save(offer));
    }

    @Transactional
    public Object updateOfferStatus(Long offerId, OfferStatus status, Long actorId, boolean admin) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        Long ownerId = offer.getArtwork().getUser() != null ? offer.getArtwork().getUser().getId() : null;
        if (!admin && (ownerId == null || !ownerId.equals(actorId))) {
            throw new UserAuthorizationException("Only the artwork owner can update this offer");
        }
        offer.setStatus(status);
        Offer saved = offerRepository.save(offer);
        if (status == OfferStatus.ACCEPTED) {
            OrderService.ArtworkLine line = new OrderService.ArtworkLine(
                    offer.getArtwork(),
                    offer.getArtwork().getUser(),
                    1,
                    offer.getAmount(),
                    offer.getAmount());
            Order order = orderService.createPendingOrder(offer.getBuyer(), line,
                    offer.getBuyer().getFirstName(), offer.getBuyer().getLastName());
            OfferAcceptResponseDto response = new OfferAcceptResponseDto();
            response.setOffer(CommerceMappers.toOfferDto(saved));
            response.setOrderId(order.getId());
            response.setPayment(null);
            return response;
        }
        return CommerceMappers.toOfferDto(saved);
    }

    public List<OfferResponseDto> getOffersForArtwork(Long artworkId) {
        return offerRepository.findByArtworkId(artworkId).stream()
                .map(CommerceMappers::toOfferDto)
                .toList();
    }

    public List<OfferResponseDto> getOffersByBuyer(Long buyerId) {
        return offerRepository.findByBuyerId(buyerId).stream()
                .map(CommerceMappers::toOfferDto)
                .toList();
    }

    public List<OfferResponseDto> getPendingForArtist(Long artistId) {
        return offerRepository.findByArtworkUserIdAndStatus(artistId, OfferStatus.PENDING).stream()
                .map(CommerceMappers::toOfferDto)
                .toList();
    }
}
