package com.taxi.rideUp.controller;

import com.taxi.rideUp.dto.NotificationRequest;
import com.taxi.rideUp.service.FcmService;
import com.taxi.rideUp.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName : com.taxi.rideUp.controller
 * fileName    : NotificationController
 * author      : hsj
 * date        : 2025. 8. 2.
 * description :
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "알림")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 전송")
    @PostMapping("/notifications")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
         notificationService.sendNotification(request);
        return ResponseEntity.ok("message sent successfully");
    }
}
