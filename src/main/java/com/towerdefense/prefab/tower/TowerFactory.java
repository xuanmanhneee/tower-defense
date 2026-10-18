package com.towerdefense.prefab.tower;

import com.towerdefense.entity.tower.TowerType;

public final class TowerFactory {

    private TowerFactory() {
    }

    public static TowerPrefab create(TowerType type) {
        return switch (type) {
            case BASIC -> new BasicTowerPrefab();
            case SPREAD -> new SpreadTowerPrefab();
            case SLOW -> new SlowTowerPrefab();
            case EXPLOSIVE -> new ExplosiveTowerPrefab();
        };
    }
}
