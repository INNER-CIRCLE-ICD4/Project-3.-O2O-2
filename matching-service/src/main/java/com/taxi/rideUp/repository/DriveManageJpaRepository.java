package com.taxi.rideUp.repository;

import com.taxi.rideUp.domain.DriveManageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : com.taxi.rideUp.repository
 * fileName    : DriveManageJpaRepository
 * author      : ckr
 * date        : 25. 8. 4.
 * description :
 */
public interface DriveManageJpaRepository extends JpaRepository<DriveManageEntity, Long> {
}
