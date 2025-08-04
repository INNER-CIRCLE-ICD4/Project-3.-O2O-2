package com.taxi.location.dto;

public record LocationDto(
    String driverId,
    double latitude,
    double longitude
) {
}
