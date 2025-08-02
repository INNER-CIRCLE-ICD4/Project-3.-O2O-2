package com.taxi.rideUp.service.external;

import com.taxi.rideUp.dto.response.external.DriverValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * packageName : com.taxi.rideUp.service.external
 * fileName    : DriveManageServiceClient
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
@FeignClient(name = "drive-manage-service", url = "${external.drive-manage-service.url}")
public interface DriveManageServiceClient {

    @GetMapping("/api/drives/{driveManageId}/validation")
    ResponseEntity<DriverValidationResponse> validateDriveManageId(@PathVariable Long driveManageId);
}
