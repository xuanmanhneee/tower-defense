package com.towerdefense.wave;

import com.towerdefense.core.PooledPrefab;
import com.towerdefense.core.Prefab;
import com.towerdefense.prefab.enemy.GruntEnemyPrefab;
import com.towerdefense.prefab.enemy.ScoutEnemyPrefab;
import com.towerdefense.prefab.enemy.TankEnemyPrefab;

import java.util.Map;

public final class EnemyRegistry {

    private final Map<String, Prefab> prefabs = Map.of(
            "grunt", pooled(new GruntEnemyPrefab(), 32),
            "scout", pooled(new ScoutEnemyPrefab(), 32),
            "tank", pooled(new TankEnemyPrefab(), 16));

    public Prefab get(String enemyId) {
        Prefab prefab = prefabs.get(enemyId);
        if (prefab == null) {
            throw new IllegalArgumentException("Unknown enemy type: " + enemyId);
        }
        return prefab;
    }

    public boolean contains(String enemyId) {
        return prefabs.containsKey(enemyId);
    }

    private static Prefab pooled(Prefab prefab, int prewarm) {
        return new PooledPrefab(new TrackedEnemyPrefab(prefab), prewarm);
    }
}
