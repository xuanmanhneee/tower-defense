package com.towerdefense.prefab.tower;

import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.entity.tower.fire.SingleShotStrategy;
import com.towerdefense.entity.tower.fire.SpreadShotDecorator;
import com.towerdefense.prefab.projectile.BasicProjectilePrefab;

import javafx.scene.paint.Color;

public class SpreadTowerPrefab extends TowerPrefab {
    @Override
    protected Color color() {
        return Color.web("#66bb6a");
    }

    @Override
    protected FireStrategy createFireStrategy() {
        return new SpreadShotDecorator(
                new SingleShotStrategy(new BasicProjectilePrefab(10)),
                new SingleShotStrategy(new BasicProjectilePrefab(3)),
                1, 15);
    }
}