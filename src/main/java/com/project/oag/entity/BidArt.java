package com.project.oag.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="bidArt")
public class BidArt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String artName;
    
    private String Artist;
    
    private String artDescription;
    
    private BigDecimal initialAmount;
    
    private LocalDateTime auctionEndTime;
    
    private BigDecimal minimumPrice;

}
