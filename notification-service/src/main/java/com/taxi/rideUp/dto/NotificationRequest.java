package com.taxi.rideUp.dto;

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
@Builder
public class NotificationRequest {
    private String targetToken;
    private String type;
}
