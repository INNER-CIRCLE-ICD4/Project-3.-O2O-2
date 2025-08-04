package com.taxi.location.api;

import com.taxi.location.dto.LocationDto;
import com.taxi.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ReadingConverter
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PutMapping("/drivers/{driverId}/location")
    public ResponseEntity<Void> addLocation(
        @PathVariable String driverId,
        @RequestBody LocationDto request
    ) {
        locationService.updateDriverLocation(driverId, request);

        return ResponseEntity.ok().build();
    }
}
