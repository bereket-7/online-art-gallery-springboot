package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Bidder;

@Repository
public interface BidderRepository extends JpaRepository<Bidder, Long>{



}
