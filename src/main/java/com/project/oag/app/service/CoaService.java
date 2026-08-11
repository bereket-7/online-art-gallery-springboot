package com.project.oag.app.service;

import com.project.oag.app.entity.CertificateOfAuthenticity;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.OrderItem;
import com.project.oag.app.repository.CertificateOfAuthenticityRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CoaService {

    private final CertificateOfAuthenticityRepository certificateRepository;

    public CoaService(CertificateOfAuthenticityRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Transactional
    public List<CertificateOfAuthenticity> issueForOrder(Order order) {
        List<CertificateOfAuthenticity> issued = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            if (certificateRepository.findByOrderItemId(item.getId()).isPresent()) {
                continue;
            }
            CertificateOfAuthenticity coa = new CertificateOfAuthenticity();
            coa.setOrderItem(item);
            coa.setVerificationCode(UUID.randomUUID().toString().replace("-", ""));
            issued.add(certificateRepository.save(coa));
        }
        return issued;
    }

    public CertificateOfAuthenticity verifyByCode(String code) {
        return certificateRepository.findByVerificationCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
    }
}
