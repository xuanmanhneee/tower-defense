package com.towerdefense.entity.tower;

public enum TowerType {
    BASIC("Basic", 25),
    SPREAD("Spread", 50),
    SLOW("Slow", 40),
    EXPLOSIVE("Explosive", 60);

    private final String displayName;
    private final int cost;

    TowerType(String displayName, int cost) {
        this.displayName = displayName;
        this.cost = cost;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCost() {
        return cost;
    }
}
