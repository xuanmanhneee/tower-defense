package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.collision.CollisionListener;
import com.towerdefense.entity.projectile.effect.HitEffect;

public class HitHandler extends Behaviour implements CollisionListener {
    private final HitEffect effect;

    public HitHandler(HitEffect effect) {
        this.effect = effect;
    }

    @Override
    public void onCollision(GameObject other) {
        effect.apply(other, gameObject, gameObject.getScene());
        gameObject.destroy();
    }
}