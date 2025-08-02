package com.taxi.rideUp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.domain
 * fileName    : NotificationHistoryEntity
 * author      : hsj
 * date        : 2025. 8. 2.
 * description :
 */
@Entity
@Table(name = "notification_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class NotificationHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private NotificationEntity notification;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private boolean result;

    @Column
    private String resultMessage;

    @Column
    private String receiver;
}
