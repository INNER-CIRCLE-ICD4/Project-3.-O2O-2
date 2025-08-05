package com.taxi.rideUp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName : com.taxi.rideUp.domain
 * fileName    : NotificationType
 * author      : hsj
 * date        : 2025. 8. 2.
 * description :
 */
@Entity
@Table(name = "notification_type")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String type;

    @Column
    private String description;

    @Column
    private String title;

    @Column
    private String message;
}

