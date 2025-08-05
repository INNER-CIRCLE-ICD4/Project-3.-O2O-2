package com.taxi.rideUp.repository;

import com.taxi.rideUp.domain.NotificationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName : com.taxi.rideUp.repository
 * fileName    : NotificationTypeRepository
 * author      : hsj
 * date        : 2025. 8. 6.
 * description :
 */
@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationTypeEntity, Long> {
    Optional<NotificationTypeEntity> findByType(String type);
}
