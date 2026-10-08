package com.towerdefense.behaviour;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;

public class LinearMotion extends Launchable {
    private final double speed;
    private Vector2 direction = new Vector2(0, 0);

    public LinearMotion(double speed) {
        this.speed = speed;
    }

    @Override
    public void launch(Vector2 dir, GameObject target) {
        this.direction = dir.normalized();
    }

    @Override
    public void update(double dt) {
        gameObject.getTransform().translate(direction.multiply(speed * dt));
    }

    @Override
    public void onReset() {
        direction = new Vector2(0, 0);
    }
}
