package com.project.oag.app.dto;

import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.ArtistWallet;
import com.project.oag.app.entity.Auction;
import com.project.oag.app.entity.Bid;
import com.project.oag.app.entity.Cart;
import com.project.oag.app.entity.CertificateOfAuthenticity;
import com.project.oag.app.entity.Collection;
import com.project.oag.app.entity.CollectionArtwork;
import com.project.oag.app.entity.Offer;
import com.project.oag.app.entity.PayoutRequest;
import com.project.oag.app.entity.Rating;
import com.project.oag.app.entity.Shipment;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.WishList;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.List;

public final class CommerceMappers {
    private CommerceMappers() {
    }

    public static CartDto toCartDto(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setQuantity(cart.getQuantity());
        dto.setArtwork(ArtworkMapper.toDto(cart.getArtwork()));
        return dto;
    }

    public static WishlistItemDto toWishlistDto(WishList wishList) {
        if (wishList == null) {
            return null;
        }
        WishlistItemDto dto = new WishlistItemDto();
        dto.setId(wishList.getId());
        dto.setArtwork(ArtworkMapper.toDto(wishList.getArtwork()));
        return dto;
    }

    public static CollectionResponseDto toCollectionDto(Collection collection, boolean includeArtworks) {
        if (collection == null) {
            return null;
        }
        CollectionResponseDto dto = new CollectionResponseDto();
        dto.setId(collection.getId());
        dto.setSlug(collection.getSlug());
        dto.setTitle(collection.getTitle());
        dto.setDescription(collection.getDescription());
        dto.setFeatured(collection.getFeatured());
        dto.setCreationDate(collection.getCreationDate());
        if (includeArtworks && collection.getArtworks() != null && Hibernate.isInitialized(collection.getArtworks())) {
            dto.setArtworks(collection.getArtworks().stream()
                    .map(CollectionArtwork::getArtwork)
                    .map(ArtworkMapper::toDto)
                    .toList());
        } else {
            dto.setArtworks(Collections.emptyList());
        }
        return dto;
    }

    public static AuctionResponseDto toAuctionDto(Auction auction) {
        if (auction == null) {
            return null;
        }
        AuctionResponseDto dto = new AuctionResponseDto();
        dto.setId(auction.getId());
        Artwork artwork = auction.getArtwork();
        dto.setArtworkId(artwork != null ? artwork.getId() : null);
        dto.setArtwork(ArtworkMapper.toDto(artwork));
        dto.setStartTime(auction.getStartTime());
        dto.setEndTime(auction.getEndTime());
        dto.setReservePrice(auction.getReservePrice());
        dto.setCurrentBid(auction.getCurrentBid());
        dto.setStatus(auction.getStatus());
        dto.setWinnerId(auction.getWinner() != null ? auction.getWinner().getId() : null);
        dto.setVersion(auction.getVersion());
        return dto;
    }

    public static BidResponseDto toBidDto(Bid bid) {
        if (bid == null) {
            return null;
        }
        BidResponseDto dto = new BidResponseDto();
        dto.setId(bid.getId());
        dto.setAuctionId(bid.getAuction() != null ? bid.getAuction().getId() : null);
        dto.setBidderId(bid.getBidder() != null ? bid.getBidder().getId() : null);
        dto.setAmount(bid.getAmount());
        dto.setBidTime(bid.getBidTime());
        return dto;
    }

    public static OfferResponseDto toOfferDto(Offer offer) {
        if (offer == null) {
            return null;
        }
        OfferResponseDto dto = new OfferResponseDto();
        dto.setId(offer.getId());
        dto.setArtworkId(offer.getArtwork() != null ? offer.getArtwork().getId() : null);
        dto.setBuyerId(offer.getBuyer() != null ? offer.getBuyer().getId() : null);
        dto.setAmount(offer.getAmount());
        dto.setStatus(offer.getStatus());
        dto.setCreatedAt(offer.getCreatedAt());
        return dto;
    }

    public static PayoutResponseDto toPayoutDto(PayoutRequest request) {
        if (request == null) {
            return null;
        }
        PayoutResponseDto dto = new PayoutResponseDto();
        dto.setId(request.getId());
        dto.setArtistId(request.getArtist() != null ? request.getArtist().getId() : null);
        dto.setAmount(request.getAmount());
        dto.setStatus(request.getStatus());
        dto.setRequestedAt(request.getRequestedAt());
        dto.setProcessedAt(request.getProcessedAt());
        dto.setExternalRef(request.getExternalRef());
        dto.setManual(request.isManual());
        return dto;
    }

    public static WalletResponseDto toWalletDto(ArtistWallet wallet) {
        if (wallet == null) {
            return null;
        }
        WalletResponseDto dto = new WalletResponseDto();
        dto.setId(wallet.getId());
        dto.setArtistId(wallet.getArtist() != null ? wallet.getArtist().getId() : null);
        dto.setBalance(wallet.getBalance());
        dto.setPendingBalance(wallet.getPendingBalance());
        dto.setVersion(wallet.getVersion());
        return dto;
    }

    public static ShipmentResponseDto toShipmentDto(Shipment shipment) {
        if (shipment == null) {
            return null;
        }
        ShipmentResponseDto dto = new ShipmentResponseDto();
        dto.setId(shipment.getId());
        dto.setOrderId(shipment.getOrder() != null ? shipment.getOrder().getId() : null);
        dto.setCarrier(shipment.getCarrier());
        dto.setTrackingNumber(shipment.getTrackingNumber());
        dto.setStatus(shipment.getStatus());
        dto.setShippedAt(shipment.getShippedAt());
        dto.setDeliveredAt(shipment.getDeliveredAt());
        return dto;
    }

    public static CertificateResponseDto toCertificateDto(CertificateOfAuthenticity certificate) {
        if (certificate == null) {
            return null;
        }
        CertificateResponseDto dto = new CertificateResponseDto();
        dto.setId(certificate.getId());
        dto.setOrderItemId(certificate.getOrderItem() != null ? certificate.getOrderItem().getId() : null);
        dto.setArtworkId(certificate.getOrderItem() != null && certificate.getOrderItem().getArtwork() != null
                ? certificate.getOrderItem().getArtwork().getId() : null);
        dto.setVerificationCode(certificate.getVerificationCode());
        dto.setIssuedAt(certificate.getIssuedAt());
        return dto;
    }

    public static ReviewResponseDto toReviewDto(Rating rating) {
        if (rating == null) {
            return null;
        }
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(rating.getId());
        dto.setArtworkId(rating.getArtwork() != null ? rating.getArtwork().getId() : null);
        User user = rating.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setUserName(String.join(" ",
                    user.getFirstName() == null ? "" : user.getFirstName(),
                    user.getLastName() == null ? "" : user.getLastName()).trim());
        }
        dto.setRating(rating.getRatingValue());
        dto.setComment(rating.getComment());
        dto.setCreatedAt(rating.getCreationDate());
        return dto;
    }

    public static List<ReviewResponseDto> toReviewDtoList(List<Rating> ratings) {
        return ratings == null ? List.of() : ratings.stream().map(CommerceMappers::toReviewDto).toList();
    }
}
