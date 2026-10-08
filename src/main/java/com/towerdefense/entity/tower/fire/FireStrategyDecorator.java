package com.towerdefense.entity.tower.fire;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;
import com.towerdefense.core.World;

public abstract class FireStrategyDecorator implements FireStrategy {

    protected final FireStrategy wrapped;

    protected FireStrategyDecorator(FireStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void fire(Vector2 origin, Vector2 direction, GameObject target, World world) {
        wrapped.fire(origin, direction, target, world);
    }
}