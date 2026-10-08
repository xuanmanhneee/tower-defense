package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;

public class AutoDestroy extends Behaviour {
    private double remaining;

    public AutoDestroy(double seconds) {
        this.remaining = seconds;
    }

    @Override
    public void update(double dt) {
        remaining -= dt;
        if (remaining <= 0)
            gameObject.destroy();
    }
}
