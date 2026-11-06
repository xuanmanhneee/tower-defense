package com.towerdefense.wave;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;

final class TrackedEnemyPrefab implements Prefab {

    private final Prefab source;

    TrackedEnemyPrefab(Prefab source) {
        this.source = source;
    }

    @Override
    public GameObject instantiate() {
        GameObject enemy = source.instantiate();
        enemy.addBehaviour(new WaveEnemyTracker());
        return enemy;
    }
}
