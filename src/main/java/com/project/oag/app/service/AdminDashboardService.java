package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.repository.PayoutRequestRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.dto.AdminDashboardKpis;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ArtworkRepository artworkRepository;
    private final PayoutRequestRepository payoutRequestRepository;

    public AdminDashboardService(UserRepository userRepository,
                               OrderRepository orderRepository,
                               ArtworkRepository artworkRepository,
                               PayoutRequestRepository payoutRequestRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.artworkRepository = artworkRepository;
        this.payoutRequestRepository = payoutRequestRepository;
    }

    public AdminDashboardKpis getKpis() {
        long totalUsers = userRepository.count();
        long totalOrders = orderRepository.count();
        long pendingArtworks = artworkRepository.findByStatus(ArtworkStatus.PENDING).size();
        long pendingPayouts = payoutRequestRepository.findByStatus(
                com.project.oag.app.dto.PayoutStatus.PENDING).size();

        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED
                        || o.getStatus() == OrderStatus.PROCESSING
                        || o.getStatus() == OrderStatus.SHIPPED
                        || o.getStatus() == OrderStatus.DELIVERED)
                .map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AdminDashboardKpis(totalUsers, totalOrders, pendingArtworks, pendingPayouts, totalRevenue);
    }
}
