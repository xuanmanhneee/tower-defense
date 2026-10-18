package com.towerdefense.prefab.tower;

import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.entity.tower.fire.SingleShotStrategy;
import com.towerdefense.prefab.projectile.BasicProjectilePrefab;

import javafx.scene.paint.Color;

public class BasicTowerPrefab extends TowerPrefab {

    @Override
    protected double fireInterval() {
        return 0.75;
    }

    @Override
    protected double range() {
        return 170;
    }

    @Override
    protected Color color() {
        return Color.web("#eef51e");
    }

    @Override
    protected FireStrategy createFireStrategy() {
        return new SingleShotStrategy(new BasicProjectilePrefab(20));
    }
}
