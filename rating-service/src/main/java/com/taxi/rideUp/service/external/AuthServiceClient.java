package com.taxi.rideUp.service.external;

import com.taxi.rideUp.dto.request.external.AverageScoreUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * packageName : com.taxi.rideUp.service.external
 * fileName    : AuthServiceClient
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
@FeignClient(name = "auth-service", url = "${external.auth-service.url}")
public interface AuthServiceClient {
    
    @PostMapping("/api/drivers/average-score")
    ResponseEntity<Void> updateAverageScore(@RequestBody AverageScoreUpdateRequest request);
}
