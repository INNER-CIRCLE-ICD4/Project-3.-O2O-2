package com.taxi.rideUp.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName : com.taxi.rideUp.dto
 * fileName    : NotificationMessage
 * author      : hsj
 * date        : 2025. 8. 3.
 * description :
 */
@Getter
@Builder
public class NotificationMessage {
    private final String token;
    private final String title;
    private final String body;
}
