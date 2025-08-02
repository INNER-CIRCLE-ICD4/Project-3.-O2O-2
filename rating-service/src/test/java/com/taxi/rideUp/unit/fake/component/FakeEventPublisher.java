package com.taxi.rideUp.unit.fake.component;

import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : com.taxi.rideUp.unit.fake.component
 * fileName    : FakeEventPublisher
 * author      : ckr
 * date        : 25. 8. 2.
 * description : 테스트용 가짜 이벤트 발행자
 */
public class FakeEventPublisher implements ApplicationEventPublisher {

    private final List<Object> publishedEvents = new ArrayList<>();

    @Override
    public void publishEvent(Object event) {
        publishedEvents.add(event);
    }

    public <T> List<T> getPublishedEvents(Class<T> eventType) {
        return publishedEvents.stream()
            .filter(eventType::isInstance)
            .map(eventType::cast)
            .toList();
    }

    public int getEventCount() {
        return publishedEvents.size();
    }

    public boolean hasEvent(Class<?> eventType) {
        return publishedEvents.stream()
            .anyMatch(eventType::isInstance);
    }
}
