package com.towerdefense.entity.tower.fire;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;
import com.towerdefense.core.World;

public interface FireStrategy {
    void fire(Vector2 origin, Vector2 direction, GameObject target, World world);
}