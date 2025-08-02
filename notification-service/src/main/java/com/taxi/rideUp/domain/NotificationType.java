package com.taxi.rideUp.domain;

/**
 * packageName : com.taxi.rideUp.domain
 * fileName    : NotificationType
 * author      : hsj
 * date        : 2025. 8. 2.
 * description :
 */
public enum NotificationType {
    // auth-service
    DRIVER_SIGNUP_APPROVED("드라이버 - 가입승인완료"),
    DRIVER_SIGNUP_REJECTED("드라이버 - 가입승인거절"),

    // matching-service
    DRIVER_CALL_CANCELLED("드라이버 - 호출취소"),
    DRIVER_MATCHED("드라이버 - 매칭완료"),
    DRIVER_RIDE_STARTED("드라이버 - 운행시작"),
    DRIVER_NEAR_DESTINATION("드라이버 - 도착지점 거의도착"),
    DRIVER_RIDE_COMPLETED("드라이버 - 운행완료"),
    PASSENGER_MATCHED("승객 - 매칭완료"),
    PASSENGER_RIDE_STARTED("승객 - 운행시작"),
    PASSENGER_NEAR_DESTINATION("승객 - 도착지점 거의도착"),
    PASSENGER_RIDE_COMPLETED("승객 - 운행완료")
    ;
    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

