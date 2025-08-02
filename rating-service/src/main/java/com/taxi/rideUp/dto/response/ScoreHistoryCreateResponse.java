package com.taxi.rideUp.dto.response;

import com.taxi.rideUp.domain.ScoreHistoryEntity;

import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.dto.response
 * fileName    : ScoreHistoryCreateResponse
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public record ScoreHistoryCreateResponse(
    Long scoreHistoryId,
    int score,
    Long driveManageId,
    LocalDateTime createdAt
) {
    public static ScoreHistoryCreateResponse from(ScoreHistoryEntity scoreHistoryEntity) {
        return new ScoreHistoryCreateResponse(
            scoreHistoryEntity.getId(),
            scoreHistoryEntity.getScore(),
            scoreHistoryEntity.getDriveManageId(),
            scoreHistoryEntity.getCreatedAt()
        );
    }
}
