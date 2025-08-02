package com.taxi.rideUp.dto;

import com.taxi.rideUp.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName : com.taxi.rideUp.dto
 * fileName    : NotificationRequest
 * author      : hsj
 * date        : 2025. 8. 3.
 * description :
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String targetToken;
    private NotificationType type;
    private String title;
    private String body;
}
