package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;

public class AutoDestroy extends Behaviour {
    private final double duration;
    private double remaining;

    public AutoDestroy(double seconds) {
        this.duration = seconds;
        this.remaining = seconds;
    }

    @Override
    public void onReset() {
        remaining = duration;
    }

    @Override
    public void update(double dt) {
        remaining -= dt;
        if (remaining <= 0)
            gameObject.destroy();
    }
}
