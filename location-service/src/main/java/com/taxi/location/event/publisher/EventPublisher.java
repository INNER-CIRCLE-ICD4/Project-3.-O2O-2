package com.taxi.location.event.publisher;

public interface EventPublisher {
    void publish(String topic, Object eventData);
}
