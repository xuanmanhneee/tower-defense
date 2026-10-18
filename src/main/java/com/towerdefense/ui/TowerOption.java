package com.towerdefense.ui;

import com.towerdefense.entity.tower.TowerType;

public record TowerOption(TowerType type) {

    public String name() {
        if (type.requiresExistingTower()) {
            return type.getDisplayName() + " upgrade";
        }
        return type.getDisplayName();
    }

    public int cost() {
        return type.getCost();
    }
}
