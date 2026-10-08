package com.towerdefense.behaviour;

import com.towerdefense.collision.CollisionListener;
import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;

public class SlowOnHit extends Behaviour implements CollisionListener {

    private final double multiplier;   // 0.6 = giảm 40% tốc độ
    private final double duration;

    public SlowOnHit(double multiplier, double duration) {
        this.multiplier = multiplier;
        this.duration = duration;
    }

    @Override
    public void onCollision(GameObject other) {
        SpeedModifiers mods = other.getBehaviour(SpeedModifiers.class);
        if (mods != null) mods.add(multiplier, duration);
    }
}
