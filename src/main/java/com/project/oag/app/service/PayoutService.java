package com.project.oag.app.service;

import com.project.oag.app.dto.PayoutStatus;
import com.project.oag.app.entity.ArtistWallet;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.OrderItem;
import com.project.oag.app.entity.PayoutRequest;
import com.project.oag.app.entity.PlatformConfig;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtistWalletRepository;
import com.project.oag.app.repository.PayoutRequestRepository;
import com.project.oag.app.repository.PlatformConfigRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PayoutService {

    private final ArtistWalletRepository walletRepository;
    private final PayoutRequestRepository payoutRequestRepository;
    private final PlatformConfigRepository platformConfigRepository;
    private final UserRepository userRepository;

    public PayoutService(ArtistWalletRepository walletRepository,
                         PayoutRequestRepository payoutRequestRepository,
                         PlatformConfigRepository platformConfigRepository,
                         UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.payoutRequestRepository = payoutRequestRepository;
        this.platformConfigRepository = platformConfigRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void creditOnPayment(Order order) {
        BigDecimal commissionRate = getCommissionRate();
        for (OrderItem item : order.getItems()) {
            BigDecimal lineTotal = item.getLineTotal();
            BigDecimal commission = lineTotal.multiply(commissionRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal artistShare = lineTotal.subtract(commission);

            ArtistWallet wallet = walletRepository.findByArtistId(item.getArtist().getId())
                    .orElseGet(() -> createWallet(item.getArtist()));
            wallet.setBalance(wallet.getBalance().add(artistShare));
            walletRepository.save(wallet);
        }
    }

    @Transactional
    public PayoutRequest requestPayout(Long artistId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GeneralException("Payout amount must be positive");
        }
        ArtistWallet wallet = walletRepository.findByArtistId(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new GeneralException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setPendingBalance(wallet.getPendingBalance().add(amount));
        walletRepository.save(wallet);

        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
        PayoutRequest request = new PayoutRequest();
        request.setArtist(artist);
        request.setAmount(amount);
        request.setStatus(PayoutStatus.PENDING);
        return payoutRequestRepository.save(request);
    }

    @Transactional
    public PayoutRequest approvePayout(Long payoutId) {
        PayoutRequest request = payoutRequestRepository.findById(payoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Payout request not found"));
        if (request.getStatus() != PayoutStatus.PENDING) {
            throw new GeneralException("Payout request is not pending");
        }

        ArtistWallet wallet = walletRepository.findByArtistId(request.getArtist().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        wallet.setPendingBalance(wallet.getPendingBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

        request.setStatus(PayoutStatus.PAID);
        request.setProcessedAt(new Timestamp(System.currentTimeMillis()));
        return payoutRequestRepository.save(request);
    }

    public ArtistWallet getWallet(Long artistId) {
        return walletRepository.findByArtistId(artistId)
                .orElseGet(() -> {
                    User artist = userRepository.findById(artistId)
                            .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
                    return createWallet(artist);
                });
    }

    public List<PayoutRequest> getPendingPayouts() {
        return payoutRequestRepository.findByStatus(PayoutStatus.PENDING);
    }

    public List<PayoutRequest> getArtistPayouts(Long artistId) {
        return payoutRequestRepository.findByArtistId(artistId);
    }

    private ArtistWallet createWallet(User artist) {
        ArtistWallet wallet = new ArtistWallet();
        wallet.setArtist(artist);
        return walletRepository.save(wallet);
    }

    private BigDecimal getCommissionRate() {
        return platformConfigRepository.findAll().stream()
                .findFirst()
                .map(PlatformConfig::getCommissionRate)
                .orElse(new BigDecimal("15.00"));
    }
}
