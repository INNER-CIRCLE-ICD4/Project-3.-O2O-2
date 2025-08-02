package com.taxi.rideUp.service;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.dto.request.ScoreHistoryCreateRequest;
import com.taxi.rideUp.dto.response.external.DriverValidationResponse;
import com.taxi.rideUp.repository.ScoreHistoryRepository;
import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;
import com.taxi.rideUp.service.external.DriveManageServiceClient;
import com.taxi.rideUp.exception.external.DriveManageValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * packageName : com.taxi.rideUp.service
 * fileName    : ScoreHistoryService
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreHistoryService {

    private final ApplicationEventPublisher eventPublisher;
    private final ScoreHistoryRepository scoreHistoryRepository;
    private final DriveManageServiceClient driveManageServiceClient;

    @Transactional
    public ScoreHistoryEntity createScoreHistory(
        Long driveManageId,
        ScoreHistoryCreateRequest scoreHistoryCreateRequest
    ) {
        // 1. DriveManageService ID 검증
        ResponseEntity<DriverValidationResponse> validationResponse = driveManageServiceClient.validateDriveManageId(driveManageId);

        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            throw new DriveManageValidationException(validationResponse);
        }

        // 2. ScoreHistory 저장
        ScoreHistoryEntity scoreHistoryEntity = ScoreHistoryEntity.createFrom(driveManageId, scoreHistoryCreateRequest);
        ScoreHistoryEntity savedScoreHistory = scoreHistoryRepository.save(scoreHistoryEntity);

        // 3. 이벤트 발행 (AuthService 평점 업데이트 + NotificationService 알림)
        ScoreCreatedEventRequest event = ScoreCreatedEventRequest.from(savedScoreHistory);
        eventPublisher.publishEvent(event);

        return savedScoreHistory;
    }
}
