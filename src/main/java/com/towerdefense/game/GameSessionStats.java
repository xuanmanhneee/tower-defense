package com.towerdefense.game;

public final class GameSessionStats {

    private int enemiesDefeated;
    private int goldEarned;

    public void recordEnemyDefeated(int reward) {
        if (reward < 0) {
            throw new IllegalArgumentException("Enemy reward cannot be negative");
        }

        enemiesDefeated++;
        goldEarned += reward;
    }

    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    public int getGoldEarned() {
        return goldEarned;
    }
}
