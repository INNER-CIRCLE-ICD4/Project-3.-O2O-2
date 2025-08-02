package com.taxi.rideUp.unit.fixture;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.dto.request.ScoreHistoryCreateRequest;
import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;
import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.unit.fixture
 * fileName    : ScoreHistoryFixture
 * author      : ckr
 * date        : 25. 8. 2.
 * description : ScoreHistory 테스트용 픽스처
 */
public class ScoreHistoryFixture {

    public static ScoreHistoryCreateRequest createRequest(Integer score) {
        return new ScoreHistoryCreateRequest(score);
    }

    public static ScoreHistoryEntity createEntity(Long id, Long driveManageId, Integer score) {
        return ScoreHistoryEntity.builder()
            .id(id)
            .driveManageId(driveManageId)
            .score(score)
            .build();
    }


    public static ScoreCreatedEventRequest createEvent(Long scoreHistoryId, Integer score) {
        return new ScoreCreatedEventRequest(
            100L,
            scoreHistoryId,
            score,
            LocalDateTime.now()
        );
    }
}
