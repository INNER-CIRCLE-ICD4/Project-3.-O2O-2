package com.taxi.rideUp.repository.impl;

import com.taxi.rideUp.domain.DriveManageEntity;
import com.taxi.rideUp.repository.DriveManageJpaRepository;
import com.taxi.rideUp.repository.DriveManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * packageName : com.taxi.rideUp.repository.impl
 * fileName    : DriveManageRepositoryImpl
 * author      : ckr
 * date        : 25. 8. 4.
 * description :
 */

@Repository
@RequiredArgsConstructor
public class DriveManageRepositoryImpl implements DriveManageRepository {

    private final DriveManageJpaRepository driveManageJpaRepository;

    @Override
    public DriveManageEntity save(DriveManageEntity driveManageEntity) {
        return driveManageJpaRepository.save(driveManageEntity);
    }
}
