package com.taxi.rideUp.unit.service;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.dto.request.ScoreHistoryCreateRequest;
import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;
import com.taxi.rideUp.dto.response.external.DriverValidationResponse;
import com.taxi.rideUp.exception.external.DriveManageValidationException;
import com.taxi.rideUp.service.ScoreHistoryService;
import com.taxi.rideUp.service.external.DriveManageServiceClient;
import com.taxi.rideUp.unit.fake.TestDiContainer;
import com.taxi.rideUp.unit.fixture.ScoreHistoryFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * packageName : com.taxi.rideUp.unit.service
 * fileName    : ScoreHistoryServiceTest
 * author      : ckr
 * date        : 25. 8. 2.
 * description : ScoreHistoryService 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ScoreHistoryService 단위 테스트")
class ScoreHistoryServiceTest {

    @Mock
    private DriveManageServiceClient driveManageServiceClient;

    private TestDiContainer testDiContainer;
    private ScoreHistoryService scoreHistoryService;

    private Long driveManageId;
    private ScoreHistoryCreateRequest request;

    @BeforeEach
    void setUp() {
        testDiContainer = new TestDiContainer();
        scoreHistoryService = testDiContainer.createScoreHistoryService(driveManageServiceClient);

        driveManageId = 100L;
        request = ScoreHistoryFixture.createRequest(2);
    }

    @Test
    @DisplayName("평점을 성공적으로 생성한다")
    void createScoreHistory_Success() {
        // given
        DriverValidationResponse validationResponse = new DriverValidationResponse("John Doe");
        ResponseEntity<DriverValidationResponse> response = ResponseEntity.ok(validationResponse);

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when
        ScoreHistoryEntity result = scoreHistoryService.createScoreHistory(driveManageId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(result.getScore()).isEqualTo(2);

        // 검증: DriveManageService 호출
        then(driveManageServiceClient).should(times(1))
            .validateDriveManageId(driveManageId);

        // 검증: 이벤트 발행
        assertThat(testDiContainer.eventPublisher.getEventCount()).isEqualTo(1);
        assertThat(testDiContainer.eventPublisher.hasEvent(ScoreCreatedEventRequest.class)).isTrue();

        List<ScoreCreatedEventRequest> publishedEvents = testDiContainer.eventPublisher.getPublishedEvents(ScoreCreatedEventRequest.class);
        assertThat(publishedEvents).hasSize(1);

        ScoreCreatedEventRequest publishedEvent = publishedEvents.get(0);
        assertThat(publishedEvent.driveManageId()).isEqualTo(driveManageId);
        assertThat(publishedEvent.scoreHistoryId()).isEqualTo(result.getId());
        assertThat(publishedEvent.score()).isEqualTo(result.getScore());
    }

    @Test
    @DisplayName("DriveManageId 검증 실패 시 예외를 발생시킨다 - 400 Bad Request")
    void createScoreHistory_ValidationFails_BadRequest() {
        // given
        ResponseEntity<DriverValidationResponse> response =
            ResponseEntity.badRequest().build();

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            scoreHistoryService.createScoreHistory(driveManageId, request))
            .isInstanceOf(DriveManageValidationException.class);

        // 검증: 이벤트 발행이 호출되지 않음
        assertThat(testDiContainer.eventPublisher.getEventCount()).isZero();
    }

    @Test
    @DisplayName("DriveManageId 검증 실패 시 예외를 발생시킨다 - 404 Not Found")
    void createScoreHistory_ValidationFails_NotFound() {
        // given
        ResponseEntity<DriverValidationResponse> response =
            ResponseEntity.notFound().build();

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            scoreHistoryService.createScoreHistory(driveManageId, request))
            .isInstanceOf(DriveManageValidationException.class);
    }

    @Test
    @DisplayName("DriveManageId 검증 실패 시 예외를 발생시킨다 - 500 Internal Server Error")
    void createScoreHistory_ValidationFails_InternalServerError() {
        // given
        ResponseEntity<DriverValidationResponse> response =
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            scoreHistoryService.createScoreHistory(driveManageId, request))
            .isInstanceOf(DriveManageValidationException.class);
    }

    @Test
    @DisplayName("DriveManageId 검증 실패 시 예외를 발생시킨다 - 401 Unauthorized")
    void createScoreHistory_ValidationFails_Unauthorized() {
        // given
        ResponseEntity<DriverValidationResponse> response =
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            scoreHistoryService.createScoreHistory(driveManageId, request))
            .isInstanceOf(DriveManageValidationException.class);

        // 검증: 이벤트 발행이 호출되지 않음
        assertThat(testDiContainer.eventPublisher.getEventCount()).isZero();
    }

    @Test
    @DisplayName("DriveManageId 검증 실패 시 예외를 발생시킨다 - 403 Forbidden")
    void createScoreHistory_ValidationFails_Forbidden() {
        // given
        ResponseEntity<DriverValidationResponse> response =
            ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when & then
        assertThatThrownBy(() ->
            scoreHistoryService.createScoreHistory(driveManageId, request))
            .isInstanceOf(DriveManageValidationException.class);

        // 검증: 이벤트 발행이 호출되지 않음
        assertThat(testDiContainer.eventPublisher.getEventCount()).isZero();
    }

    @Test
    @DisplayName("최소 점수(-2)로 평점을 성공적으로 생성한다")
    void createScoreHistory_MinimumScore_Success() {
        // given
        ScoreHistoryCreateRequest minScoreRequest = ScoreHistoryFixture.createRequest(-2);
        DriverValidationResponse validationResponse = new DriverValidationResponse("John Doe");
        ResponseEntity<DriverValidationResponse> response = ResponseEntity.ok(validationResponse);

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when
        ScoreHistoryEntity result = scoreHistoryService.createScoreHistory(driveManageId, minScoreRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(result.getScore()).isEqualTo(-2);

        // 검증: 이벤트 발행
        assertThat(testDiContainer.eventPublisher.getEventCount()).isEqualTo(1);
        List<ScoreCreatedEventRequest> publishedEvents = testDiContainer.eventPublisher.getPublishedEvents(ScoreCreatedEventRequest.class);
        ScoreCreatedEventRequest publishedEvent = publishedEvents.getFirst();
        assertThat(publishedEvent.score()).isEqualTo(-2);
    }

    @Test
    @DisplayName("최대 점수(2)로 평점을 성공적으로 생성한다")
    void createScoreHistory_MaximumScore_Success() {
        // given
        ScoreHistoryCreateRequest maxScoreRequest = ScoreHistoryFixture.createRequest(2);
        DriverValidationResponse validationResponse = new DriverValidationResponse("John Doe");
        ResponseEntity<DriverValidationResponse> response = ResponseEntity.ok(validationResponse);

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when
        ScoreHistoryEntity result = scoreHistoryService.createScoreHistory(driveManageId, maxScoreRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(result.getScore()).isEqualTo(2);

        // 검증: 이벤트 발행
        assertThat(testDiContainer.eventPublisher.getEventCount()).isEqualTo(1);
        List<ScoreCreatedEventRequest> publishedEvents = testDiContainer.eventPublisher.getPublishedEvents(ScoreCreatedEventRequest.class);
        ScoreCreatedEventRequest publishedEvent = publishedEvents.getFirst();
        assertThat(publishedEvent.score()).isEqualTo(2);
    }

    @Test
    @DisplayName("0점으로 평점을 성공적으로 생성한다")
    void createScoreHistory_ZeroScore_Success() {
        // given
        ScoreHistoryCreateRequest zeroScoreRequest = ScoreHistoryFixture.createRequest(0);
        DriverValidationResponse validationResponse = new DriverValidationResponse("John Doe");
        ResponseEntity<DriverValidationResponse> response = ResponseEntity.ok(validationResponse);

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when
        ScoreHistoryEntity result = scoreHistoryService.createScoreHistory(driveManageId, zeroScoreRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(result.getScore()).isEqualTo(0);

        // 검증: 이벤트 발행
        assertThat(testDiContainer.eventPublisher.getEventCount()).isEqualTo(1);
        List<ScoreCreatedEventRequest> publishedEvents = testDiContainer.eventPublisher.getPublishedEvents(ScoreCreatedEventRequest.class);
        ScoreCreatedEventRequest publishedEvent = publishedEvents.getFirst();
        assertThat(publishedEvent.score()).isEqualTo(0);
    }

    @Test
    @DisplayName("동일한 driveManageId로 여러 번 평점 생성 시 각각 고유한 ID를 가진다")
    void createScoreHistory_MultipleSameDriveManageId_UniqueIds() {
        // given
        DriverValidationResponse validationResponse = new DriverValidationResponse("John Doe");
        ResponseEntity<DriverValidationResponse> response = ResponseEntity.ok(validationResponse);

        given(driveManageServiceClient.validateDriveManageId(driveManageId))
            .willReturn(response);

        // when - 같은 driveManageId로 두 번 평점 생성
        ScoreHistoryEntity result1 = scoreHistoryService.createScoreHistory(driveManageId, ScoreHistoryFixture.createRequest(1));
        ScoreHistoryEntity result2 = scoreHistoryService.createScoreHistory(driveManageId, ScoreHistoryFixture.createRequest(-1));

        // then
        assertThat(result1.getId()).isEqualTo(1L);
        assertThat(result2.getId()).isEqualTo(2L);
        assertThat(result1.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(result2.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(result1.getScore()).isEqualTo(1);
        assertThat(result2.getScore()).isEqualTo(-1);

        // 검증: 이벤트가 두 번 발행됨
        assertThat(testDiContainer.eventPublisher.getEventCount()).isEqualTo(2);
        List<ScoreCreatedEventRequest> publishedEvents = testDiContainer.eventPublisher.getPublishedEvents(ScoreCreatedEventRequest.class);
        assertThat(publishedEvents).hasSize(2);
    }
}
