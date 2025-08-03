package com.taxi.rideUp.unit.fake.repository;

import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.repository.ScoreHistoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : com.taxi.rideUp.unit.fake.repository
 * fileName    : FakeScoreHistoryRepository
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public class FakeScoreHistoryRepository implements ScoreHistoryRepository {

    private final List<ScoreHistoryEntity> storedScoreHistoryEntities = new ArrayList<>();

    @Override
    public ScoreHistoryEntity save(ScoreHistoryEntity scoreHistoryEntity) {
        if(scoreHistoryEntity.getId() == null) {
            long idSequence = storedScoreHistoryEntities.stream().mapToLong(ScoreHistoryEntity::getId).max().orElse(0L);

            ScoreHistoryEntity newScoreHistoryEntity = ScoreHistoryEntity.builder()
                .id(++idSequence)
                .driveManageId(scoreHistoryEntity.getDriveManageId())
                .score(scoreHistoryEntity.getScore())
                .createdAt(scoreHistoryEntity.getCreatedAt())
                .build();
            storedScoreHistoryEntities.add(newScoreHistoryEntity);
            return newScoreHistoryEntity;
        } else {
            storedScoreHistoryEntities.removeIf(storedScoreHistoryEntity ->
                storedScoreHistoryEntity.getId().equals(scoreHistoryEntity.getId())
            );
            storedScoreHistoryEntities.add(scoreHistoryEntity);
            return scoreHistoryEntity;
        }
    }
}
