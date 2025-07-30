package com.taxi.auth.entity;

public enum DriveStatus {
    RESTING,   // 휴식
    PENDING,   // 운행 대기
    ONLINE,    // 운행 가능
    DRIVING,   // 운행 중
    END        // 운행 종료
}
