package com.project.oag.dto;

import java.math.BigDecimal;

public record AdminDashboardKpis(
        long totalUsers,
        long totalOrders,
        long pendingArtworks,
        long pendingPayouts,
        BigDecimal totalRevenue
) {
}
