package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.Vector2;

public class TravelLimit extends Behaviour {
    private final double maxDistance;
    private Vector2 origin;

    public TravelLimit(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public void start() {
        origin = gameObject.getTransform().getPosition();
    }

    @Override
    public void update(double dt) {
        Vector2 p = gameObject.getTransform().getPosition();
        if (p.subtract(origin).lengthSquared() >= maxDistance * maxDistance) {
            gameObject.destroy();
        }
    }

    @Override
    public void onReset() {
        origin = gameObject.getTransform().getPosition();
    }
}
