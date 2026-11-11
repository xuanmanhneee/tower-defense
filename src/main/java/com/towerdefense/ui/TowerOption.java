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

    public String description() {
        return switch (type) {
            case BASIC -> "Reliable single-target damage";
            case SPREAD -> "Covers clustered enemies";
            case SLOW -> "Controls fast enemies";
            case EXPLOSIVE -> "Area damage on impact";
        };
    }
}
