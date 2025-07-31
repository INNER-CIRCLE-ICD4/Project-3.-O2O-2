package com.taxi.rideUp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * packageName : com.taxi.rideUp.domain
 * fileName    : ScoreHistoryEntity
 * author      : ckr
 * date        : 25. 7. 31.
 * description :
 */

@Entity
@Table(name = "score_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ScoreHistoryEntity {
    @Id
    @Column(name = "score_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long driveManageId;
}
