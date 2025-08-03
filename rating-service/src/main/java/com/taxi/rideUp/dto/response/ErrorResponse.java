package com.taxi.rideUp.dto.response;

/**
 * packageName : com.taxi.rideUp.dto.response
 * fileName    : ErrorResponse
 * author      : ckr
 * date        : 25. 8. 3.
 * description :
 */
public record ErrorResponse(
    String error,
    String message
) {
}
