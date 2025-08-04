package com.taxi.rideUp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.domain
 * fileName    : DriveManageEntity
 * author      : ckr
 * date        : 25. 8. 4.
 * description :
 */

@Entity
@Table(name = "drive_manage")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DriveManageEntity {
    @Id
    @Column(name = "drive_manage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Point pickupLocation;

    @Column(nullable = false)
    private String pickupAddress;

    @Column(nullable = false)
    private Point destinationLocation;

    @Column(nullable = false)
    private String destinationAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    @Column(nullable = false)
    private LocalDateTime matchRequestedAt;

    @Column
    private LocalDateTime matchedAt;

    @Column
    private LocalDateTime rideStartedAt;

    @Column
    private LocalDateTime rideEndedAt;

    @Column
    private Double actualDistance;

    @Column
    private LocalDateTime cancelledAt;

    @Column
    private Integer estimatedFare;

    @Column
    private Integer baseFare;

    @Column
    private Integer distanceFare;

    @Column
    private Integer timeFare;

    @Column
    private Integer totalFare;

    @Column(nullable = false)
    private Long passengerId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
