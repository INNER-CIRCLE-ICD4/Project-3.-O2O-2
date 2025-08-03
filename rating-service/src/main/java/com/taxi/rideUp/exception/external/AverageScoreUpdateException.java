package com.taxi.rideUp.exception.external;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * packageName : com.taxi.rideUp.service.external.exception
 * fileName    : AverageScoreUpdateException
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */

@Getter
public class AverageScoreUpdateException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;

    public AverageScoreUpdateException(ResponseEntity<Void> responseEntity) {
        super("AverageScoreUpdate API Failed. response: " + responseEntity.getBody());
        this.httpStatusCode = responseEntity.getStatusCode();
    }
}
