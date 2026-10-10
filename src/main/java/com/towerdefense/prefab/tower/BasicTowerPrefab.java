package com.towerdefense.prefab.tower;

import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.entity.tower.fire.SingleShotStrategy;
import com.towerdefense.prefab.projectile.ProjectilePools;

import javafx.scene.paint.Color;

public class BasicTowerPrefab extends TowerPrefab {

    @Override
    protected Color color() {
        return Color.web("#eef51e");
    }

    @Override
    protected FireStrategy createFireStrategy() {
    return new SingleShotStrategy(ProjectilePools.BASIC);
    }
}