package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;
import com.towerdefense.core.World;
import com.towerdefense.entity.tower.fire.FireStrategy;

public class FireControl extends Behaviour {
    private final Targeting targeting;
    private final FireStrategy strategy;
    private final double interval;
    private double cooldown = 0;

    public FireControl(Targeting targeting, FireStrategy strategy, double interval) {
        this.targeting = targeting;
        this.strategy = strategy;
        this.interval = interval;
    }

    @Override
    public void update(double dt) {
        cooldown -= dt;
        if (cooldown > 0)
            return;

        GameObject target = targeting.getCurrentTarget();
        if (target == null)
            return;

        Vector2 origin = gameObject.getTransform().getPosition();
        
        Vector2 direction = target.getTransform()
                .getPosition()
                .subtract(origin)
                .normalized();

        World world = gameObject.getScene();

        strategy.fire(origin, direction, target, world);

        cooldown = interval;
    }
}