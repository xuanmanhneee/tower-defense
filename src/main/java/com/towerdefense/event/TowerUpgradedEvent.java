package com.towerdefense.event;

import com.towerdefense.core.GameEvent;
import com.towerdefense.entity.tower.TowerType;

public record TowerUpgradedEvent(TowerType towerType) implements GameEvent {
}
