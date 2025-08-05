package com.taxi.location.service;

import com.taxi.common.event.DriverLocationUpdatedEvent;
import com.taxi.location.dto.LocationDto;
import com.taxi.location.event.publisher.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    private LocationService locationService;

    @Mock
    private EventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        // @InjectMocks 대신 수동으로 객체를 생성하고 Mock 객체를 주입합니다.
        // @Value로 주입되는 topicName도 테스트용 값을 직접 전달합니다.
        locationService = new LocationService(eventPublisher, "test-topic");
    }

    @Test
    @DisplayName("위치 업데이트 요청 시 Kafka로 이벤트가 발행되어야 한다")
    public void updateDriverLocationAndPublish() {
        // given
        String driverId = "driver-123";
        LocationDto request = new LocationDto(driverId, 37.5, 127.0);

        // when
        locationService.updateDriverLocation(driverId, request);

        // then
        ArgumentCaptor<DriverLocationUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(DriverLocationUpdatedEvent.class);
        // setUp에서 주입한 "test-topic"으로 발행되었는지 검증합니다.
        verify(eventPublisher).publish(org.mockito.ArgumentMatchers.eq("test-topic"), eventCaptor.capture());

        DriverLocationUpdatedEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.driverId()).isEqualTo(driverId);
        assertThat(publishedEvent.latitude()).isEqualTo(request.latitude());
        assertThat(publishedEvent.longitude()).isEqualTo(request.longitude());
    }
}
