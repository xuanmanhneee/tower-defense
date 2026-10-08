package com.towerdefense.entity.projectile.effect;

import com.towerdefense.behaviour.Health;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.World;

public class DamageOnHit implements HitEffect {
    private final double damage;
    public DamageOnHit(double damage) { this.damage = damage; }

    @Override
    public void apply(GameObject target, GameObject projectile, World world) {
        target.getBehaviour(Health.class).damage(damage);
    }
}
