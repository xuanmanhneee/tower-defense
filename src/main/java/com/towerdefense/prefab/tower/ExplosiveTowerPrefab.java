package com.towerdefense.prefab.tower;

import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.entity.tower.fire.SingleShotStrategy;
import com.towerdefense.prefab.projectile.ProjectilePools;

import javafx.scene.paint.Color;

public class ExplosiveTowerPrefab extends TowerPrefab {
    @Override
    protected double fireInterval() {
        return 1.1;
    }

    @Override
    protected double range() {
        return 165;
    }

    @Override
    protected com.towerdefense.entity.tower.TowerType towerType() {
        return com.towerdefense.entity.tower.TowerType.EXPLOSIVE;
    }

    @Override
    protected Color color() {
        return Color.web("#9d1607");
    }

    @Override
    protected FireStrategy createFireStrategy() {
        return new SingleShotStrategy(ProjectilePools.EXPLOSIVE);
    }
}
