package com.taxi.rideUp.integration.service;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.dto.request.ScoreHistoryCreateRequest;
import com.taxi.rideUp.dto.response.external.DriverValidationResponse;
import com.taxi.rideUp.repository.ScoreHistoryJpaRepository;
import com.taxi.rideUp.service.ScoreHistoryService;
import com.taxi.rideUp.service.external.AuthServiceClient;
import com.taxi.rideUp.service.external.DriveManageServiceClient;
import com.taxi.rideUp.service.external.NotificationServiceClient;
import com.taxi.rideUp.unit.fixture.ScoreHistoryFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.timeout;

/**
 * packageName : com.taxi.rideUp.integration.service
 * fileName    : ScoreHistoryServiceIntegrationTest
 * author      : ckr
 * date        : 25. 8. 2.
 * description : ScoreHistoryService 통합 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ScoreHistoryService 통합 테스트")
class ScoreHistoryServiceIntegrationTest {

    @Autowired
    private ScoreHistoryService scoreHistoryService;

    @Autowired
    private ScoreHistoryJpaRepository scoreHistoryRepository;

    @MockitoBean
    private DriveManageServiceClient driveManageServiceClient;

    @MockitoBean
    private AuthServiceClient authServiceClient;

    @MockitoBean
    private NotificationServiceClient notificationServiceClient;

    @AfterEach
    void tearDown() {
        scoreHistoryRepository.deleteAll();
    }

    @Test
    @DisplayName("평점 생성 시 데이터베이스에 저장되고 이벤트가 발행된다")
    void createScoreHistory_SavesDataAndPublishesEvents() {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(2);

        // Mock 외부 서비스 응답
        DriverValidationResponse validationResponse = new DriverValidationResponse("John Doe");
        given(driveManageServiceClient.validateDriveManageId(anyLong()))
            .willReturn(ResponseEntity.ok(validationResponse));
        given(authServiceClient.updateAverageScore(any()))
            .willReturn(ResponseEntity.ok().build());
        given(notificationServiceClient.sendScoreCreatedNotification(any()))
            .willReturn(ResponseEntity.ok().build());

        // when
        ScoreHistoryEntity result = scoreHistoryService.createScoreHistory(driveManageId, request);

        // then
        // 반환값 검증
        assertThat(result.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(result.getScore()).isEqualTo(2);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();

        // 데이터베이스 저장 검증
        assertThat(scoreHistoryRepository.count()).isEqualTo(1);
        var savedEntity = scoreHistoryRepository.findById(result.getId()).orElseThrow();
        assertThat(savedEntity.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(savedEntity.getScore()).isEqualTo(2);

        // 비동기 이벤트 처리 검증 (타임아웃 설정)
        then(authServiceClient).should(timeout(5000).times(1))
            .updateAverageScore(any());
        then(notificationServiceClient).should(timeout(5000).times(1))
            .sendScoreCreatedNotification(any());
    }

    @Test
    @DisplayName("음수 평점으로도 정상적으로 생성된다")
    void createScoreHistory_WithNegativeScore() {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(-2);

        DriverValidationResponse validationResponse = new DriverValidationResponse("John Doe");
        given(driveManageServiceClient.validateDriveManageId(anyLong()))
            .willReturn(ResponseEntity.ok(validationResponse));
        given(authServiceClient.updateAverageScore(any()))
            .willReturn(ResponseEntity.ok().build());
        given(notificationServiceClient.sendScoreCreatedNotification(any()))
            .willReturn(ResponseEntity.ok().build());

        // when
        ScoreHistoryEntity result = scoreHistoryService.createScoreHistory(driveManageId, request);

        // then
        assertThat(result.getScore()).isEqualTo(-2);

        // 데이터베이스 저장 검증
        var savedEntity = scoreHistoryRepository.findById(result.getId()).orElseThrow();
        assertThat(savedEntity.getScore()).isEqualTo(-2);
    }

    @Test
    @DisplayName("여러 평점을 연속으로 생성할 수 있다")
    void createScoreHistory_Multiple() {
        // given
        Long driveManageId1 = 100L;
        Long driveManageId2 = 200L;
        ScoreHistoryCreateRequest request1 = ScoreHistoryFixture.createRequest(2);
        ScoreHistoryCreateRequest request2 = ScoreHistoryFixture.createRequest(-1);

        DriverValidationResponse validationResponse = new DriverValidationResponse("John Doe");
        given(driveManageServiceClient.validateDriveManageId(anyLong()))
            .willReturn(ResponseEntity.ok(validationResponse));
        given(authServiceClient.updateAverageScore(any()))
            .willReturn(ResponseEntity.ok().build());
        given(notificationServiceClient.sendScoreCreatedNotification(any()))
            .willReturn(ResponseEntity.ok().build());

        // when
        scoreHistoryService.createScoreHistory(driveManageId1, request1);
        scoreHistoryService.createScoreHistory(driveManageId2, request2);

        // then
        assertThat(scoreHistoryRepository.count()).isEqualTo(2);

        var entities = scoreHistoryRepository.findAll();
        assertThat(entities).hasSize(2);
        assertThat(entities).extracting("driveManageId")
            .containsExactlyInAnyOrder(driveManageId1, driveManageId2);
        assertThat(entities).extracting("score")
            .containsExactlyInAnyOrder(2, -1);
    }
}
