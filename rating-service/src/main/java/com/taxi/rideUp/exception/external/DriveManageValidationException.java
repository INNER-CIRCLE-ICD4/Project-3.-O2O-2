package com.taxi.rideUp.exception.external;

import com.taxi.rideUp.dto.response.external.DriverValidationResponse;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * packageName : com.taxi.rideUp.service.external.exception
 * fileName    : DriveManageValidationException
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
@Getter
public class DriveManageValidationException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;

    public DriveManageValidationException(ResponseEntity<DriverValidationResponse> responseEntity) {
        super("DriveManageValidation API Failed. response: " + responseEntity.getBody());
        this.httpStatusCode = responseEntity.getStatusCode();
    }
}
