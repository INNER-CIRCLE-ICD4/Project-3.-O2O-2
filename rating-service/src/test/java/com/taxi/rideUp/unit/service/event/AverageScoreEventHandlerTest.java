package com.taxi.rideUp.unit.service.event;

import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;
import com.taxi.rideUp.dto.request.external.AverageScoreUpdateRequest;
import com.taxi.rideUp.exception.external.AverageScoreUpdateException;
import com.taxi.rideUp.service.event.AverageScoreEventHandler;
import com.taxi.rideUp.service.external.AuthServiceClient;
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
 * fileName    : AverageScoreEventHandlerTest
 * author      : ckr
 * date        : 25. 8. 2.
 * description : AverageScoreEventHandler 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AverageScoreEventHandler 단위 테스트")
class AverageScoreEventHandlerTest {

    @Mock
    private AuthServiceClient authServiceClient;

    @InjectMocks
    private AverageScoreEventHandler averageScoreEventHandler;

    private ScoreCreatedEventRequest event;

    @BeforeEach
    void setUp() {
        event = ScoreHistoryFixture.createEvent(1L, 2);
    }

    @Test
    @DisplayName("평점 업데이트 이벤트를 성공적으로 처리한다")
    void handleScoreCreatedEventRequest_Success() {
        // given
        ResponseEntity<Void> response = ResponseEntity.ok().build();
        given(authServiceClient.updateAverageScore(any(AverageScoreUpdateRequest.class)))
            .willReturn(response);

        // when
        averageScoreEventHandler.handleScoreCreatedEventRequest(event);

        // then
        ArgumentCaptor<AverageScoreUpdateRequest> requestCaptor =
            ArgumentCaptor.forClass(AverageScoreUpdateRequest.class);
        then(authServiceClient).should(times(1))
            .updateAverageScore(requestCaptor.capture());

        AverageScoreUpdateRequest capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.scoreHistoryId()).isEqualTo(event.scoreHistoryId());
        assertThat(capturedRequest.score()).isEqualTo(event.score());
        assertThat(capturedRequest.createdAt()).isEqualTo(event.createdAt());
    }

    @Test
    @DisplayName("AuthService 호출 실패 시 예외를 발생시킨다 - 400 Bad Request")
    void handleScoreCreatedEventRequest_AuthServiceFails_BadRequest() {
        // given
        ResponseEntity<Void> response = ResponseEntity.badRequest().build();
        given(authServiceClient.updateAverageScore(any(AverageScoreUpdateRequest.class)))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            averageScoreEventHandler.handleScoreCreatedEventRequest(event))
            .isInstanceOf(AverageScoreUpdateException.class);
    }

    @Test
    @DisplayName("AuthService 호출 실패 시 예외를 발생시킨다 - 500 Internal Server Error")
    void handleScoreCreatedEventRequest_AuthServiceFails_InternalServerError() {
        // given
        ResponseEntity<Void> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        given(authServiceClient.updateAverageScore(any(AverageScoreUpdateRequest.class)))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            averageScoreEventHandler.handleScoreCreatedEventRequest(event))
            .isInstanceOf(AverageScoreUpdateException.class);
    }
}
