package com.taxi.location.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.GeoIndexed;

@Getter
@AllArgsConstructor
@RedisHash("driver_location")
public class DriverLocation {
    @Id
    private String driverId;

    @GeoIndexed
    private Point location;
}
