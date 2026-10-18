package com.towerdefense.prefab.tower;

import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.entity.tower.fire.SingleShotStrategy;
import com.towerdefense.entity.tower.fire.SpreadShotDecorator;
import com.towerdefense.prefab.projectile.BasicProjectilePrefab;

import javafx.scene.paint.Color;

public class SpreadTowerPrefab extends TowerPrefab {
    @Override
    protected double fireInterval() {
        return 0.9;
    }

    @Override
    protected double range() {
        return 160;
    }

    @Override
    protected Color color() {
        return Color.web("#66bb6a");
    }

    @Override
    protected FireStrategy createFireStrategy() {
        return new SpreadShotDecorator(
                new SingleShotStrategy(new BasicProjectilePrefab(14)),
                new SingleShotStrategy(new BasicProjectilePrefab(7)),
                1, 12);
    }
}
