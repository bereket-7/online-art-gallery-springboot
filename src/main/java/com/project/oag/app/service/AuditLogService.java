package com.project.oag.app.service;

import com.project.oag.app.entity.AuditLog;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.AuditLogRepository;
import com.project.oag.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AuditLog log(Long adminId, String action, String entityType, Long entityId, String details) {
        AuditLog entry = new AuditLog();
        entry.setAction(action);
        entry.setEntityType(entityType);
        entry.setEntityId(entityId);
        entry.setDetails(details);
        if (adminId != null) {
            User admin = userRepository.findById(adminId).orElse(null);
            entry.setAdmin(admin);
        }
        return auditLogRepository.save(entry);
    }
}
