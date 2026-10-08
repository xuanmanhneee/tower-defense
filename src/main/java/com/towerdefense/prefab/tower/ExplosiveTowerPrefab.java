package com.towerdefense.prefab.tower;

import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.entity.tower.fire.SingleShotStrategy;
import com.towerdefense.prefab.projectile.ExplosiveProjectilePrefab;

import javafx.scene.paint.Color;

public class ExplosiveTowerPrefab extends TowerPrefab {
    @Override
    protected Color color() {
        return Color.web("#9d1607");
    }

    @Override
    protected FireStrategy createFireStrategy() {
        return new SingleShotStrategy(new ExplosiveProjectilePrefab());
    }
}
