package com.towerdefense.ui;

import com.towerdefense.entity.tower.TowerType;

public record TowerOption(TowerType type) {

    public String name() {
        return type.getDisplayName();
    }

    public int cost() {
        return type.getCost();
    }
}
