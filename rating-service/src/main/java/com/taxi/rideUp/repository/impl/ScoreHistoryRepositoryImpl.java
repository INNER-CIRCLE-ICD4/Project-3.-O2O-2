package com.taxi.rideUp.repository.impl;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.repository.ScoreHistoryJpaRepository;
import com.taxi.rideUp.repository.ScoreHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * packageName : com.taxi.rideUp.repository.impl
 * fileName    : ScoreHistoryRepositoryImpl
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */

@Repository
@RequiredArgsConstructor
public class ScoreHistoryRepositoryImpl implements ScoreHistoryRepository {

    private final ScoreHistoryJpaRepository scoreHistoryJpaRepository;

    @Override
    public ScoreHistoryEntity save(ScoreHistoryEntity scoreHistoryEntity) {
        return scoreHistoryJpaRepository.save(scoreHistoryEntity);
    }
}
