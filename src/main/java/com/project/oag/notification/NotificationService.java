package com.project.oag.notification;

import com.project.oag.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUser(user);
    }
    public void markNotificationAsRead(Long notificationId) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        optionalNotification.ifPresent(notification -> {
            notification.markAsRead();
            notificationRepository.save(notification);
        });
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

}
