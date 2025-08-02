package com.taxi.rideUp.service.external;

import com.taxi.rideUp.dto.request.external.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * packageName : com.taxi.rideUp.service.external
 * fileName    : NotificationServiceClient
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
@FeignClient(name = "notification-service", url = "${external.notification-service.url}")
public interface NotificationServiceClient {
    
    @PostMapping("/api/notifications/score-created")
    ResponseEntity<Void> sendScoreCreatedNotification(@RequestBody NotificationRequest request);
}
