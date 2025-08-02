package com.taxi.rideUp.repository;

import com.taxi.rideUp.domain.ScoreHistoryEntity;

/**
 * packageName : com.taxi.rideUp.repository
 * fileName    : ScoreHistoryRepository
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */


public interface ScoreHistoryRepository {
    ScoreHistoryEntity save(ScoreHistoryEntity scoreHistoryEntity);
}
