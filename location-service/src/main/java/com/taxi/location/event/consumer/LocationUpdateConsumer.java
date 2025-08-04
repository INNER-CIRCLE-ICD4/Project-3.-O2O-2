package com.taxi.location.event.consumer;

import com.taxi.common.event.DriverLocationUpdatedEvent;
import com.taxi.location.domain.DriverLocation;
import com.taxi.location.port.out.DriverLocationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationUpdateConsumer {
    // DLT 메시지 전용 로거를 생성합니다. (logback-spring.xml에 정의됨)
    private static final Logger dltLogger = LoggerFactory.getLogger("DltLogger");

    private final DriverLocationPort driverLocationPort;

    @KafkaListener(topics = "${app.kafka.topic.driver-location}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeLocationUpdate(DriverLocationUpdatedEvent event) {
        log.info("Location update received for driver: {}", event.driverId());

        Point point = new Point(event.longitude(), event.latitude());
        DriverLocation driverLocation = new DriverLocation(event.driverId(), point);
        driverLocationPort.save(driverLocation);
    }

    /**
     * Dead Letter Topic을 구독하여 최종 실패한 메시지를 별도 로그 파일에 저장
     */
    @KafkaListener(topics = "${app.kafka.topic.driver-location-dlt}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeDlt(DriverLocationUpdatedEvent event) {
        // 일반 에러 로그 대신, DLT 전용 로거를 사용하여 파일에 기록합니다.
        dltLogger.error("Failed driver location Event: {}", event.toString());
    }
}
