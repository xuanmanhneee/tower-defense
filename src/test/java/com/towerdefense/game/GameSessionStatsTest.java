package com.towerdefense.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameSessionStatsTest {

    @Test
    void recordsKillsAndRewardGold() {
        GameSessionStats stats = new GameSessionStats();

        assertEquals(0, stats.getEnemiesDefeated());
        assertEquals(0, stats.getGoldEarned());

        stats.recordEnemyDefeated(12);
        stats.recordEnemyDefeated(8);

        assertEquals(2, stats.getEnemiesDefeated());
        assertEquals(20, stats.getGoldEarned());
    }

    @Test
    void rejectsNegativeRewardWithoutChangingCounters() {
        GameSessionStats stats = new GameSessionStats();

        assertThrows(IllegalArgumentException.class, () -> stats.recordEnemyDefeated(-1));
        assertEquals(0, stats.getEnemiesDefeated());
        assertEquals(0, stats.getGoldEarned());
    }
}
