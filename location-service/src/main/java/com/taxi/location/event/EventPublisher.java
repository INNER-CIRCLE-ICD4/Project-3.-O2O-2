package com.taxi.location.event;

public interface EventPublisher {
    void publish(String topic, Object eventData);
}
