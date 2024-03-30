package com.project.oag.app.repository;

import com.project.oag.app.model.Notification;
import com.project.oag.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUser(User user);
}
