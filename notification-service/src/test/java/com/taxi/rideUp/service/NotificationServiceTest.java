package com.taxi.rideUp.service;

import com.taxi.rideUp.domain.DeliveryStatus;
import com.taxi.rideUp.domain.NotificationEntity;
import com.taxi.rideUp.domain.NotificationHistoryEntity;
import com.taxi.rideUp.domain.NotificationType;
import com.taxi.rideUp.dto.NotificationRequest;
import com.taxi.rideUp.repository.NotificationHistoryRepository;
import com.taxi.rideUp.repository.NotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;

    @MockitoBean
    private FcmService fcmService;

    /**
     * 테스트가 끝날때마다 생성된 데이터 삭제
     */
    @AfterEach
    void tearDown() {
        notificationHistoryRepository.deleteAllInBatch();
        notificationRepository.deleteAllInBatch();
    }

    @DisplayName("알림 전송에 성공하면 SENT 상태로 저장되고 히스토리가 기록된다.")
    @Test
    void createNotificationSuccess() throws Exception {
        // given
        String successResponse = "success-message-id";
        NotificationRequest request = createNotificationRequest("테스트 제목", "테스트 내용", "test-token", NotificationType.DRIVER_MATCHED);

        given(fcmService.sendMessage(any(NotificationRequest.class)))
            .willReturn("FAKE_SUCCESS");

        // when
        notificationService.sendNotification(request);

        // then
        List<NotificationEntity> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(1)
            .extracting("title", "deliveryStatus")
            .containsExactly(tuple("테스트 제목", DeliveryStatus.SENT));

        List<NotificationHistoryEntity> histories = notificationHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
            .extracting("result", "receiver")
            .containsExactly(tuple(true, "test-token"));

        verify(fcmService, times(1)).sendMessage(any(NotificationRequest.class));
    }

    @DisplayName("첫번째 시도 실패 후 두번째 시도에서 성공하면 성공 히스토리와 실패 히스토리가 모두 기록된다.")
    @Test
    void createNotificationRetrySuccess() throws Exception {
        // given
        String errorMessage = "Network timeout";
        String successResponse = "success-message-id";
        NotificationRequest request = createNotificationRequest("재시도 테스트", "재시도 내용", "retry-token", NotificationType.DRIVER_MATCHED);

        given(fcmService.sendMessage(any(NotificationRequest.class)))
            .willThrow(new RuntimeException(errorMessage)) // 첫번째 호출 - 예외발생
            .willReturn(successResponse); // 두번째 호출 - 성공

        // when
        notificationService.sendNotification(request);

        // then
        List<NotificationEntity> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(1)
            .extracting("deliveryStatus")
            .containsExactly(DeliveryStatus.SENT);

        List<NotificationHistoryEntity> histories = notificationHistoryRepository.findAll();
        assertThat(histories).hasSize(2);

        // 실패 히스토리 검증
        NotificationHistoryEntity failureHistory = histories.stream()
            .filter(h -> !h.isResult())
            .findFirst()
            .orElseThrow();
        assertThat(failureHistory.getResultMessage()).isEqualTo(errorMessage);
        assertThat(failureHistory.getReceiver()).isEqualTo("retry-token");

        // 성공 히스토리 검증
        NotificationHistoryEntity successHistory = histories.stream()
            .filter(NotificationHistoryEntity::isResult)
            .findFirst()
            .orElseThrow();
        assertThat(successHistory.getResultMessage()).isEqualTo(successResponse);
        assertThat(successHistory.getReceiver()).isEqualTo("retry-token");

        verify(fcmService, times(2)).sendMessage(any(NotificationRequest.class));
    }

    @DisplayName("3번 모두 실패하면 FAILED 상태로 저장되고 3개의 실패 히스토리가 기록된다.")
    @Test
    void createNotificationAllRetryFailed() throws Exception {
        // given
        String errorMessage = "Persistent network error";
        NotificationRequest request = createNotificationRequest("실패 테스트", "실패 내용", "failed-token", NotificationType.DRIVER_MATCHED);

        given(fcmService.sendMessage(any(NotificationRequest.class)))
            .willThrow(new RuntimeException(errorMessage));

        // when
        notificationService.sendNotification(request);

        // then
        List<NotificationEntity> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(1);

        NotificationEntity notification = notifications.get(0);
        assertThat(notification.getTitle()).isEqualTo("실패 테스트");
        assertThat(notification.getMessage()).isEqualTo("실패 내용");
        assertThat(notification.getDeliveryStatus()).isEqualTo(DeliveryStatus.FAILED);

        List<NotificationHistoryEntity> histories = notificationHistoryRepository.findAll();
        assertThat(histories).hasSize(3);

        // 모든 히스토리가 실패로 기록되었는지 검증
        histories.forEach(history -> {
            assertThat(history.isResult()).isFalse();
            assertThat(history.getResultMessage()).isEqualTo(errorMessage);
            assertThat(history.getReceiver()).isEqualTo("failed-token");
            assertThat(history.getNotification().getId()).isEqualTo(notification.getId());
        });

        verify(fcmService, times(3)).sendMessage(any(NotificationRequest.class));
    }

    @DisplayName("여러 종류의 알림 타입으로 알림을 생성할 수 있다.")
    @Test
    void createNotificationWithDifferentTypes() throws Exception {
        // given
        String successResponse = "success-message-id";
        NotificationRequest rideRequest = createNotificationRequest("매칭 완료", "매칭 완료 내용", "ride-token", NotificationType.DRIVER_MATCHED);
        NotificationRequest paymentRequest = createNotificationRequest("운행 완료", "운행 완료 내용", "ride-token", NotificationType.PASSENGER_RIDE_COMPLETED);

        given(fcmService.sendMessage(any(NotificationRequest.class)))
            .willReturn(successResponse);

        // when
        notificationService.sendNotification(rideRequest);
        notificationService.sendNotification(paymentRequest);

        // then
        List<NotificationEntity> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(2);

        NotificationEntity rideNotification = notifications.stream()
            .filter(n -> n.getNotificationType() == NotificationType.DRIVER_MATCHED)
            .findFirst()
            .orElseThrow();
        assertThat(rideNotification.getTitle()).isEqualTo("매칭 완료");
        assertThat(rideNotification.getDeliveryStatus()).isEqualTo(DeliveryStatus.SENT);

        NotificationEntity paymentNotification = notifications.stream()
            .filter(n -> n.getNotificationType() == NotificationType.PASSENGER_RIDE_COMPLETED)
            .findFirst()
            .orElseThrow();
        assertThat(paymentNotification.getTitle()).isEqualTo("운행 완료");
        assertThat(paymentNotification.getDeliveryStatus()).isEqualTo(DeliveryStatus.SENT);

        List<NotificationHistoryEntity> histories = notificationHistoryRepository.findAll();
        assertThat(histories).hasSize(2);

        histories.forEach(history -> {
            assertThat(history.isResult()).isTrue();
            assertThat(history.getResultMessage()).isEqualTo(successResponse);
        });

        verify(fcmService, times(2)).sendMessage(any(NotificationRequest.class));
    }

    private NotificationRequest createNotificationRequest(String title, String body, String targetToken, NotificationType type) {
        return NotificationRequest.builder()
            .title(title)
            .body(body)
            .type(type)
            .targetToken(targetToken)
            .build();
    }
}
