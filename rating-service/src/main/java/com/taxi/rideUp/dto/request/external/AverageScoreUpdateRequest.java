package com.taxi.rideUp.dto.request.external;

import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;

import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.dto.request.external
 * fileName    : AverageScoreRequest
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public record AverageScoreUpdateRequest(
    Long scoreHistoryId,
    Integer score,
    LocalDateTime createdAt
) {
    public static AverageScoreUpdateRequest from(ScoreCreatedEventRequest scoreCreatedEventRequest) {
        return new AverageScoreUpdateRequest(
            scoreCreatedEventRequest.scoreHistoryId(),
            scoreCreatedEventRequest.score(),
            scoreCreatedEventRequest.createdAt()
        );
    }
}
