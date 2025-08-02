package com.taxi.rideUp.dto.request.event;

import com.taxi.rideUp.domain.ScoreHistoryEntity;

import java.time.LocalDateTime;

/**
  * packageName : com.taxi.rideUp.service.event
  * fileName    : ScoreCreatedEvent
  * author      : ckr
  * date        : 25. 8. 2.
  * description :
  */

public record ScoreCreatedEventRequest(
    Long driveManageId,
    Long scoreHistoryId,
    Integer score,
    LocalDateTime createdAt
) {
    public static ScoreCreatedEventRequest from(ScoreHistoryEntity scoreHistoryEntity) {
        return new ScoreCreatedEventRequest(
            scoreHistoryEntity.getDriveManageId(),
            scoreHistoryEntity.getId(),
            scoreHistoryEntity.getScore(),
            scoreHistoryEntity.getCreatedAt()
        );
    }
}
