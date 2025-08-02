package com.taxi.rideUp.unit.domain;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.dto.request.ScoreHistoryCreateRequest;
import com.taxi.rideUp.unit.fixture.ScoreHistoryFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.taxi.rideUp.unit.domain
 * fileName    : ScoreHistoryEntityTest
 * author      : ckr
 * date        : 25. 8. 2.
 * description : ScoreHistoryEntity 단위 테스트
 */
@DisplayName("ScoreHistoryEntity 단위 테스트")
class ScoreHistoryEntityTest {

    @Test
    @DisplayName("ScoreHistoryEntity를 생성할 수 있다")
    void createScoreHistoryEntity() {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(2);

        // when
        ScoreHistoryEntity entity = ScoreHistoryEntity.createFrom(driveManageId, request);

        // then
        assertThat(entity.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(entity.getScore()).isEqualTo(2);
        assertThat(entity.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("음수 평점으로 ScoreHistoryEntity를 생성할 수 있다")
    void createScoreHistoryEntityWithNegativeScore() {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(-2);

        // when
        ScoreHistoryEntity entity = ScoreHistoryEntity.createFrom(driveManageId, request);

        // then
        assertThat(entity.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(entity.getScore()).isEqualTo(-2);
        assertThat(entity.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("빌더 패턴으로 ScoreHistoryEntity를 생성할 수 있다")
    void createScoreHistoryEntityWithBuilder() {
        // given & when
        ScoreHistoryEntity entity = ScoreHistoryFixture.createEntity(1L, 100L, 1);

        // then
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getDriveManageId()).isEqualTo(100L);
        assertThat(entity.getScore()).isEqualTo(1);
        assertThat(entity.getCreatedAt()).isNull();
    }
}
