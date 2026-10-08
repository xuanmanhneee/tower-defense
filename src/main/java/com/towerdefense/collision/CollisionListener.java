package com.towerdefense.collision;

import com.towerdefense.core.GameObject;

public interface CollisionListener {
    void onCollision(GameObject other);
}
