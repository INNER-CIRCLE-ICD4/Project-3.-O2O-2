package com.taxi.rideUp.dto.request.external;

import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;

import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.dto.request.external
 * fileName    : NotificationSendRequest
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public record NotificationSendRequest(
    Long scoreHistoryId,
    Integer score,
    LocalDateTime createdAt
) {
    public static NotificationSendRequest from(ScoreCreatedEventRequest scoreCreatedEventRequest) {
        return new NotificationSendRequest(
            scoreCreatedEventRequest.scoreHistoryId(),
            scoreCreatedEventRequest.score(),
            scoreCreatedEventRequest.createdAt()
        );
    }
}
