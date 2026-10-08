package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.collision.CollisionListener;

public class GoalReceiver extends Behaviour implements CollisionListener {
    @Override
    public void onCollision(GameObject enemy) {
        enemy.destroy();
    }
}
