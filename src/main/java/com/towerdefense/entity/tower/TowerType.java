package com.towerdefense.entity.tower;

public enum TowerType {
    BASIC("Basic", 25, null),
    SPREAD("Spread", 50, BASIC),
    SLOW("Slow", 40, BASIC),
    EXPLOSIVE("Explosive", 60, BASIC);

    private final String displayName;
    private final int cost;
    private final TowerType prerequisite;

    TowerType(String displayName, int cost, TowerType prerequisite) {
        this.displayName = displayName;
        this.cost = cost;
        this.prerequisite = prerequisite;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCost() {
        return cost;
    }

    public boolean requiresExistingTower() {
        return prerequisite != null;
    }

    public boolean canBuildOn(TowerType existingTower) {
        return prerequisite == existingTower;
    }
}
