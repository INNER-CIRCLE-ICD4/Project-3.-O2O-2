package com.taxi.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private String vehicleNumber; // 차량 번호

    @Column(nullable = false)
    private String vehicleModel; // 차량 모델

    @Column(nullable = false)
    private String vehicleType; // 차량 종류

    @Column(precision = 3, scale = 2)
    private BigDecimal averagePoint;

    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatus; // 승인 상태

    @Enumerated(EnumType.STRING)
    private DriveStatus driveStatus; // 운행 상태

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
