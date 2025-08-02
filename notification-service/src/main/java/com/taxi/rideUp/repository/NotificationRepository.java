package com.taxi.rideUp.repository;

import com.taxi.rideUp.domain.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName : com.taxi.rideUp.repository
 * fileName    : NotificationRepository
 * author      : hsj
 * date        : 2025. 8. 2.
 * description :
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
