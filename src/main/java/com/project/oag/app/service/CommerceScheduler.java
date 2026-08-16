package com.project.oag.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommerceScheduler {

    private final OrderService orderService;
    private final AuctionService auctionService;

    public CommerceScheduler(OrderService orderService, AuctionService auctionService) {
        this.orderService = orderService;
        this.auctionService = auctionService;
    }

    @Scheduled(fixedDelay = 300000)
    public void sweepAbandonedOrders() {
        int cancelled = orderService.cancelAbandonedPendingOrders();
        if (cancelled > 0) {
            log.info("Abandoned order sweeper cancelled {} pending orders", cancelled);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void closeExpiredAuctions() {
        int closed = auctionService.closeExpiredAuctions();
        if (closed > 0) {
            log.info("Closed {} expired auctions", closed);
        }
    }
}
