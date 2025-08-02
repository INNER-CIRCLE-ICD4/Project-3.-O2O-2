package com.taxi.rideUp.repository;

import com.taxi.rideUp.domain.NotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName : com.taxi.rideUp.repository
 * fileName    : NotificationHistoryRepository
 * author      : hsj
 * date        : 2025. 8. 3.
 * description :
 */
@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistoryEntity, Long> {
}
