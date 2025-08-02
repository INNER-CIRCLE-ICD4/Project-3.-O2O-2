package com.taxi.rideUp.exception.external;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * packageName : com.taxi.rideUp.service.external.exception
 * fileName    : NotificationSendException
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */

@Getter
public class NotificationSendException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;

    public NotificationSendException(ResponseEntity<Void> responseEntity) {
        super("NotificationSend API Failed. response: " + responseEntity.getBody());
        this.httpStatusCode = responseEntity.getStatusCode();
    }
}
