package com.taxi.rideUp.dto.request.external;


import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;

/**
 * packageName : com.taxi.rideUp.dto.request.external
 * fileName    : NotificationSendRequest
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public record NotificationSendRequest(
    String targetToken,
    String type
) {
    public static NotificationSendRequest from(ScoreCreatedEventRequest request) {
        return new NotificationSendRequest(
            request.notificationTargetToken(),
            request.notificationType()
        );
    }
}
