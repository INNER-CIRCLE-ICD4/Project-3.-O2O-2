package com.taxi.rideUp.controller;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.dto.request.ScoreHistoryCreateRequest;
import com.taxi.rideUp.dto.response.ScoreHistoryCreateResponse;
import com.taxi.rideUp.service.ScoreHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName : com.taxi.rideUp.controller
 * fileName    : ScoreHistoryController
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */

@RestController
@RequestMapping("/drive-manage")
@RequiredArgsConstructor
class ScoreHistoryController {

    private final ScoreHistoryService scoreHistoryService;

    @PostMapping("/{driveManageId}/score-histories")
    public ResponseEntity<ScoreHistoryCreateResponse> postScoreHistory(
        @PathVariable Long driveManageId,
        @Valid @RequestBody ScoreHistoryCreateRequest scoreHistoryCreateRequest
    ) {
        ScoreHistoryEntity scoreHistory = scoreHistoryService.createScoreHistory(driveManageId, scoreHistoryCreateRequest);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ScoreHistoryCreateResponse.from(scoreHistory));
    }
}
