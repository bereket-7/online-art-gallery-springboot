package com.project.oag.app.repository;

import com.project.oag.app.dto.PayoutStatus;
import com.project.oag.app.entity.PayoutRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayoutRequestRepository extends JpaRepository<PayoutRequest, Long> {
    List<PayoutRequest> findByArtistId(Long artistId);

    List<PayoutRequest> findByStatus(PayoutStatus status);
}
