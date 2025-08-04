package com.taxi.location.adapter.out.persistence;

import com.taxi.location.domain.DriverLocation;
import com.taxi.location.port.out.DriverLocationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisDriverLocationAdapter implements DriverLocationPort {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String GEO_KEY = "drivers:location";

    @Override
    public void save(DriverLocation driverLocation) {
        redisTemplate.opsForGeo().add(
            GEO_KEY,
            driverLocation.getLocation(),
            driverLocation.getDriverId()
        );
    }

    @Override
    public List<DriverLocation> findNearby(Point point, Distance distance) {
        var geoResults = redisTemplate.opsForGeo().radius(GEO_KEY, point, distance);

        return Optional.ofNullable(geoResults)
            .map(results -> results.getContent().stream()
                .map(result -> {
                    String driverId = (String) result.getContent().getName();
                    // 위치 정보가 없을 수도 있으므로 Optional로 처리
                    return Optional.ofNullable(redisTemplate.opsForGeo().position(GEO_KEY, driverId))
                        .filter(positions -> !positions.isEmpty())
                        .map(positions -> new DriverLocation(driverId, positions.get(0)));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }
}
