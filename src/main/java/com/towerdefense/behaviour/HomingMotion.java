package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;

public class HomingMotion extends Behaviour {
    private final double speed;
    private GameObject target;
    private Vector2 lastDirection; // mục tiêu chết thì bay tiếp hướng cuối

    public HomingMotion(double speed) {
        this.speed = speed;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    @Override
    public void onReset() {
        target = null;
        lastDirection = null;
    }

    @Override
    public void update(double dt) {
        Vector2 pos = gameObject.getTransform().getPosition();
        if (target != null && target.isAlive()) {
            lastDirection = target.getTransform().getPosition().subtract(pos).normalized();
        }
        if (lastDirection == null)
            return;
        gameObject.getTransform().translate(lastDirection.multiply(speed * dt));
    }
}