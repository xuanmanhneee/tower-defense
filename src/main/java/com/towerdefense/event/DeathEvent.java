package com.towerdefense.event;

import com.towerdefense.core.GameEvent;
import com.towerdefense.core.GameObject;

public record DeathEvent(GameObject source, int reward) implements GameEvent {
}