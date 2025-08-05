package com.taxi.rideUp.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.taxi.rideUp.domain.NotificationTypeEntity;
import com.taxi.rideUp.dto.NotificationRequest;
import org.springframework.stereotype.Service;

/**
 * packageName : com.taxi.rideUp.service
 * fileName    : FcmService
 * author      : hsj
 * date        : 2025. 8. 3.
 * description :
 */

@Service
public class FcmService {
    public String sendMessage(NotificationRequest request, NotificationTypeEntity type) throws Exception {
        Message message = Message.builder()
            .setToken(request.getTargetToken())
            .setNotification(Notification.builder()
                .setTitle(type.getTitle())
                .setBody(type.getMessage())
                .build())
            .build();
        return FirebaseMessaging.getInstance().send(message);
    }
}

