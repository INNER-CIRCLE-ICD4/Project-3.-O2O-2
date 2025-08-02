package com.taxi.rideUp.unit.fake;

import com.taxi.rideUp.service.ScoreHistoryService;
import com.taxi.rideUp.service.external.DriveManageServiceClient;
import com.taxi.rideUp.unit.fake.component.FakeEventPublisher;
import com.taxi.rideUp.unit.fake.repository.FakeScoreHistoryRepository;

/**
 * packageName : com.taxi.rideUp.unit.fake
 * fileName    : TestDiContainer
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public class TestDiContainer {
    public final FakeScoreHistoryRepository scoreHistoryRepository = new FakeScoreHistoryRepository();
    public final FakeEventPublisher eventPublisher = new FakeEventPublisher();
    
    public ScoreHistoryService createScoreHistoryService(DriveManageServiceClient driveManageServiceClient) {
        return new ScoreHistoryService(
            eventPublisher,
            scoreHistoryRepository,
            driveManageServiceClient
        );
    }
}
