package com.taxi.rideUp.unit.service.event;

import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;
import com.taxi.rideUp.dto.request.external.NotificationSendRequest;
import com.taxi.rideUp.exception.external.NotificationSendException;
import com.taxi.rideUp.service.event.NotificationEventHandler;
import com.taxi.rideUp.service.external.NotificationServiceClient;
import com.taxi.rideUp.unit.fixture.ScoreHistoryFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * packageName : com.taxi.rideUp.unit.service.event
 * fileName    : NotificationEventHandlerTest
 * author      : ckr
 * date        : 25. 8. 2.
 * description : NotificationEventHandler 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationEventHandler 단위 테스트")
class NotificationEventHandlerTest {

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @InjectMocks
    private NotificationEventHandler notificationEventHandler;

    private ScoreCreatedEventRequest event;

    @BeforeEach
    void setUp() {
        event = ScoreHistoryFixture.createEvent(1L, 2);
    }

    @Test
    @DisplayName("알림 전송 이벤트를 성공적으로 처리한다")
    void handleScoreCreatedEventRequest_Success() {
        // given
        ResponseEntity<Void> response = ResponseEntity.ok().build();
        given(notificationServiceClient.sendScoreCreatedNotification(any(NotificationSendRequest.class)))
            .willReturn(response);

        // when
        notificationEventHandler.handleScoreCreatedEventRequest(event);

        // then
        ArgumentCaptor<NotificationSendRequest> requestCaptor =
            ArgumentCaptor.forClass(NotificationSendRequest.class);
        then(notificationServiceClient).should(times(1))
            .sendScoreCreatedNotification(requestCaptor.capture());

        NotificationSendRequest capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.targetToken()).isEqualTo(event.notificationTargetToken());
        assertThat(capturedRequest.type()).isEqualTo(event.notificationType());
    }

    @Test
    @DisplayName("NotificationService 호출 실패 시 예외를 발생시킨다 - 400 Bad Request")
    void handleScoreCreatedEventRequest_NotificationServiceFails_BadRequest() {
        // given
        ResponseEntity<Void> response = ResponseEntity.badRequest().build();
        given(notificationServiceClient.sendScoreCreatedNotification(any(NotificationSendRequest.class)))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            notificationEventHandler.handleScoreCreatedEventRequest(event))
            .isInstanceOf(NotificationSendException.class);
    }

    @Test
    @DisplayName("NotificationService 호출 실패 시 예외를 발생시킨다 - 500 Internal Server Error")
    void handleScoreCreatedEventRequest_NotificationServiceFails_InternalServerError() {
        // given
        ResponseEntity<Void> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        given(notificationServiceClient.sendScoreCreatedNotification(any(NotificationSendRequest.class)))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            notificationEventHandler.handleScoreCreatedEventRequest(event))
            .isInstanceOf(NotificationSendException.class);
    }
}
