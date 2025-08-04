package com.taxi.location.service;

import com.taxi.common.event.DriverLocationUpdatedEvent;
import com.taxi.location.dto.LocationDto;
import com.taxi.location.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final EventPublisher eventPublisher;

    @Value("${app.kafka.topic.driver-location-update}")
    private String topicName;

    public void updateDriverLocation(String driverId, LocationDto request) {
        var event = new DriverLocationUpdatedEvent(
            driverId,
            request.latitude(),
            request.longitude()
        );

        eventPublisher.publish(topicName, event);
    }
}
