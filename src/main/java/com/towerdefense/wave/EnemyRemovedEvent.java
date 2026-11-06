package com.towerdefense.wave;

import com.towerdefense.core.GameEvent;
import com.towerdefense.core.GameObject;

public record EnemyRemovedEvent(GameObject enemy) implements GameEvent {
}
