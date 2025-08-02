package com.taxi.rideUp.exception;

/**
 * packageName : com.taxi.rideUp.exception
 * fileName    : NotificationSendException
 * author      : hsj
 * date        : 2025. 8. 3.
 * description :
 */
public class NotificationException extends RuntimeException {
    public NotificationException() {
        super("Failed to send notification");
    }
}

