package com.taxi.rideUp.repository;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : com.taxi.rideUp.repository
 * fileName    : ScoreHistoryJpaRepository
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */

public interface ScoreHistoryJpaRepository extends JpaRepository<ScoreHistoryEntity, Long> {
}
