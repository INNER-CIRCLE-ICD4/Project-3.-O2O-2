package com.taxi.rideUp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * packageName : com.taxi.rideUp.dto.request
 * fileName    : ScoreHistoryCreateRequest
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public record ScoreHistoryCreateRequest(
    @NotNull
    @Min(value = -2, message = "Score must be between -2 and 2")
    @Max(value = 2, message = "Score must be between -2 and 2")
    Integer score
) {
}
