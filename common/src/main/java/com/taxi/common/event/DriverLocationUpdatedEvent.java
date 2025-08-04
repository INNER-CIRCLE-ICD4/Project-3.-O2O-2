package com.taxi.common.event;

import java.time.LocalDateTime;

public record DriverLocationUpdatedEvent(
    String driverId,
    double latitude,
    double longitude,
    LocalDateTime timestamp
) {
    public DriverLocationUpdatedEvent(String driverId, double latitude, double longitude) {
        this(driverId, latitude, longitude, LocalDateTime.now());
    }
}
