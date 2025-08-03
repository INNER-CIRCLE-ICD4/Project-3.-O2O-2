package com.taxi.rideUp.integration.controller;

import com.taxi.rideUp.dto.request.ScoreHistoryCreateRequest;
import com.taxi.rideUp.dto.response.ScoreHistoryCreateResponse;
import com.taxi.rideUp.dto.response.external.DriverValidationResponse;
import com.taxi.rideUp.repository.ScoreHistoryJpaRepository;
import com.taxi.rideUp.service.external.AuthServiceClient;
import com.taxi.rideUp.service.external.DriveManageServiceClient;
import com.taxi.rideUp.service.external.NotificationServiceClient;
import com.taxi.rideUp.unit.fixture.ScoreHistoryFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * packageName : com.taxi.rideUp.integration.controller
 * fileName    : ScoreHistoryControllerIntegrationTest
 * author      : ckr
 * date        : 25. 8. 2.
 * description : ScoreHistoryController 통합 테스트
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("ScoreHistoryController 통합 테스트")
class ScoreHistoryControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScoreHistoryJpaRepository scoreHistoryRepository;

    @MockitoBean
    private DriveManageServiceClient driveManageServiceClient;

    @MockitoBean
    private AuthServiceClient authServiceClient;

    @MockitoBean
    private NotificationServiceClient notificationServiceClient;

    @BeforeEach
    void setUp() {
        scoreHistoryRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        scoreHistoryRepository.deleteAll();
    }

    @Test
    @DisplayName("평점 생성 전체 플로우가 정상적으로 동작한다")
    void createScoreHistory_FullFlow_Success() {
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ScoreHistoryCreateRequest> entity = new HttpEntity<>(request, headers);

        String url = "http://localhost:" + port + "/drive-manages/" + driveManageId + "/score-histories";
        ResponseEntity<ScoreHistoryCreateResponse> response = restTemplate.postForEntity(url, entity, ScoreHistoryCreateResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().driveManageId()).isEqualTo(driveManageId);
        assertThat(response.getBody().score()).isEqualTo(2);
        assertThat(response.getBody().scoreHistoryId()).isNotNull();

        // 데이터베이스 검증
        assertThat(scoreHistoryRepository.count()).isEqualTo(1);
        var savedEntity = scoreHistoryRepository.findAll().getFirst();
        assertThat(savedEntity.getDriveManageId()).isEqualTo(driveManageId);
        assertThat(savedEntity.getScore()).isEqualTo(2);
    }

    @Test
    @DisplayName("DriveManageId 검증 실패 시 데이터가 저장되지 않는다")
    void createScoreHistory_ValidationFails_NoDataSaved() {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(2);

        // Mock 외부 서비스 실패 응답
        given(driveManageServiceClient.validateDriveManageId(anyLong()))
            .willReturn(ResponseEntity.badRequest().build());

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ScoreHistoryCreateRequest> entity = new HttpEntity<>(request, headers);

        String url = "http://localhost:" + port + "/drive-manages/" + driveManageId + "/score-histories";
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // 데이터베이스 검증 - 데이터가 저장되지 않았는지 확인
        assertThat(scoreHistoryRepository.count()).isZero();
    }

    @Test
    @DisplayName("유효하지 않은 평점 범위로 요청 시 데이터가 저장되지 않는다")
    void createScoreHistory_InvalidScore_NoDataSaved() {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(5); // 유효 범위 초과

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ScoreHistoryCreateRequest> entity = new HttpEntity<>(request, headers);

        String url = "http://localhost:" + port + "/drive-manages/" + driveManageId + "/score-histories";
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // 데이터베이스 검증 - 데이터가 저장되지 않았는지 확인
        assertThat(scoreHistoryRepository.count()).isZero();
    }
}
