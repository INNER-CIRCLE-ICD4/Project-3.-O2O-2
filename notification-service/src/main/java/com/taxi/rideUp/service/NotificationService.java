package com.taxi.rideUp.service;

import com.google.firebase.messaging.Notification;
import com.taxi.rideUp.domain.DeliveryStatus;
import com.taxi.rideUp.domain.NotificationEntity;
import com.taxi.rideUp.domain.NotificationHistoryEntity;
import com.taxi.rideUp.dto.NotificationRequest;
import com.taxi.rideUp.repository.NotificationHistoryRepository;
import com.taxi.rideUp.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.service
 * fileName    : NotificationService
 * author      : hsj
 * date        : 2025. 8. 3.
 * description :
 */

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;

    @Transactional
    public void sendNotification(NotificationRequest request) {
        NotificationEntity notification = saveNewNotification(request);

        int retryCount = 0;
        boolean isSuccess = false;

        // 알림 전송 실패시 최대 2번까지 재시도
        while (retryCount < 3 && !isSuccess) {
            try {
                String response = fcmService.sendMessage(request);
                notification.updateStatus(DeliveryStatus.SENT);
                isSuccess = true;
                saveNotificationHistory(request.getTargetToken(), response, isSuccess, notification);
            } catch (Exception e) {
                retryCount++;
                notification.updateStatus(DeliveryStatus.FAILED);
                saveNotificationHistory(request.getTargetToken(), e.getMessage(), isSuccess, notification);
            }
        }
    }

    private void saveNotificationHistory(String targetToken, String response, boolean isSuccess, NotificationEntity notification) {
        NotificationHistoryEntity notificationHistoryEntity = NotificationHistoryEntity.builder()
            .notification(notification)
            .result(isSuccess)
            .resultMessage(response)
            .receiver(targetToken)
            .build();

        notificationHistoryRepository.save(notificationHistoryEntity);
    }

    private NotificationEntity saveNewNotification(NotificationRequest request) {
        NotificationEntity notification = NotificationEntity.builder()
            .title(request.getTitle())
            .message(request.getBody())
            .notificationType(request.getType())
            .deliveryStatus(DeliveryStatus.PENDING)
            .deliveryCount(0)
            .createdAt(LocalDateTime.now())
            .build();

        return notificationRepository.save(notification);
    }
}

