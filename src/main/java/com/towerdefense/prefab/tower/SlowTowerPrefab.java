package com.towerdefense.prefab.tower;

import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.entity.tower.fire.SingleShotStrategy;
import com.towerdefense.prefab.projectile.ProjectilePools;

import javafx.scene.paint.Color;

public class SlowTowerPrefab extends TowerPrefab {
    @Override
    protected double fireInterval() {
        return 0.85;
    }

    @Override
    protected double range() {
        return 180;
    }

    @Override
    protected Color color() {
        return Color.web("#1ebff5");
    }

    @Override
    protected FireStrategy createFireStrategy() {
        return new SingleShotStrategy(ProjectilePools.SLOW);
    }
}
