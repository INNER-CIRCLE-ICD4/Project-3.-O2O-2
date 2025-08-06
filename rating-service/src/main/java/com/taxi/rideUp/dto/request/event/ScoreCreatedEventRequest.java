package com.taxi.rideUp.dto.request.event;

import com.taxi.rideUp.domain.ScoreHistoryEntity;

import java.time.LocalDateTime;

/**
  * packageName : com.taxi.rideUp.service.event
  * fileName    : AverageScoreUpdateEventRequest
  * author      : ckr
  * date        : 25. 8. 2.
  * description :
  */

public record AverageScoreUpdateEventRequest(
    Long driveManageId,
    Long scoreHistoryId,
    Integer score,
    LocalDateTime createdAt
) {
    public static AverageScoreUpdateEventRequest from(ScoreHistoryEntity scoreHistoryEntity) {
        return new AverageScoreUpdateEventRequest(
            scoreHistoryEntity.getDriveManageId(),
            scoreHistoryEntity.getId(),
            scoreHistoryEntity.getScore(),
            scoreHistoryEntity.getCreatedAt()
        );
    }
}
