package com.taxi.rideUp.service.event;

import com.taxi.rideUp.dto.request.event.ScoreCreatedEventRequest;
import com.taxi.rideUp.dto.request.external.AverageScoreUpdateRequest;
import com.taxi.rideUp.exception.external.AverageScoreUpdateException;
import com.taxi.rideUp.service.external.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * packageName : com.taxi.rideUp.service.event
 * fileName    : AverageScoreEventHandler
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AverageScoreEventHandler {

    private final AuthServiceClient authServiceClient;

    @Async
    @EventListener
    public void handleScoreCreatedEventRequest(ScoreCreatedEventRequest event) {
        AverageScoreUpdateRequest request = AverageScoreUpdateRequest.from(event);
        ResponseEntity<Void> response = authServiceClient.updateAverageScore(request);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AverageScoreUpdateException(response);
        }
    }
}
