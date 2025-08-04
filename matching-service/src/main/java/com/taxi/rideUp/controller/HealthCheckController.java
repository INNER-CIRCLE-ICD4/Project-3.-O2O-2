package com.taxi.rideUp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName : com.taxi.rideUp.controller
 * fileName    : HealthCheckController
 * author      : ckr
 * date        : 25. 8. 4.
 * description :
 */
@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Hello world! This is Matching Service.");
    }
}
