package com.taxi.rideUp.repository;

import com.taxi.rideUp.domain.DriveManageEntity;

/**
 * packageName : com.taxi.rideUp.repository
 * fileName    : DriveManageRepository
 * author      : ckr
 * date        : 25. 8. 4.
 * description :
 */

public interface DriveManageRepository {
    DriveManageEntity save(DriveManageEntity driveManageEntity);
}
