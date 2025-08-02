package com.taxi.rideUp.unit.fake;

import com.taxi.rideUp.unit.fake.repository.FakeScoreHistoryRepository;

/**
 * packageName : com.taxi.rideUp.unit.fake
 * fileName    : TestDiContainer
 * author      : ckr
 * date        : 25. 8. 2.
 * description :
 */
public class TestDiContainer {
    FakeScoreHistoryRepository scoreHistoryRepository = new FakeScoreHistoryRepository();
}
