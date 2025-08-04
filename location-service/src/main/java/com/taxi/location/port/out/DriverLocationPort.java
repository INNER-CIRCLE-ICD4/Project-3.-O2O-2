package com.taxi.location.port.out;

import com.taxi.location.domain.DriverLocation;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import java.util.List;

public interface DriverLocationPort {
    void save(DriverLocation driverLocation);
    List<DriverLocation> findNearby(Point point, Distance distance);
}
