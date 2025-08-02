package com.taxi.rideUp.dto.request.external;

import com.taxi.rideUp.domain.ScoreHistoryEntity;

import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.dto.request.external
 * fileName    : NotificationRequest
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public record NotificationRequest(
    Long scoreHistoryId,
    Integer score,
    LocalDateTime createdAt
) {
    public static NotificationRequest from(ScoreHistoryEntity scoreHistoryEntity) {
        return new NotificationRequest(
            scoreHistoryEntity.getId(),
            scoreHistoryEntity.getScore(),
            scoreHistoryEntity.getCreatedAt()
        );
    }
}
