package com.taxi.rideUp.service.event;

import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;
import com.taxi.rideUp.dto.request.external.NotificationRequest;
import com.taxi.rideUp.exception.external.NotificationSendException;
import com.taxi.rideUp.service.external.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * packageName : com.taxi.rideUp.service.event
 * fileName    : NotificationEventHandler
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationServiceClient notificationServiceClient;

    @Async
    @EventListener
    public void handleScoreCreatedEventRequest(ScoreCreatedEventRequest event) {
        NotificationRequest request = new NotificationRequest(
            event.scoreHistoryId(),
            event.score(),
            event.createdAt()
        );

        ResponseEntity<Void> response = notificationServiceClient.sendScoreCreatedNotification(request);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new NotificationSendException(response);
        }
    }
}
