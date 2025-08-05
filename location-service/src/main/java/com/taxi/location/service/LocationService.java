package com.taxi.location.service;

import com.taxi.common.event.DriverLocationUpdatedEvent;
import com.taxi.location.dto.LocationDto;
import com.taxi.location.event.publisher.EventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private final EventPublisher eventPublisher;
    private final String topicName;

    public LocationService(EventPublisher eventPublisher,
                           @Value("${app.kafka.topic.driver-location}") String topicName) {
        this.eventPublisher = eventPublisher;
        this.topicName = topicName;
    }

    public void updateDriverLocation(String driverId, LocationDto request) {
        var event = new DriverLocationUpdatedEvent(
            driverId,
            request.latitude(),
            request.longitude()
        );

        eventPublisher.publish(topicName, event);
    }
}
