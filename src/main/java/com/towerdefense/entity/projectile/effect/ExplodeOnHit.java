package com.towerdefense.entity.projectile.effect;

import com.towerdefense.behaviour.Health;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;
import com.towerdefense.core.World;

public class ExplodeOnHit extends HitEffectDecorator {
    private final double radius, damage;

    public ExplodeOnHit(HitEffect inner, double radius, double damage) {
        super(inner);
        this.radius = radius;
        this.damage = damage;
    }

    @Override
    public void apply(GameObject target, GameObject projectile, World world) {
        super.apply(target, projectile, world);
        Vector2 center = projectile.getTransform().getPosition();
        double r2 = radius * radius;

        for (GameObject e : world.findObjectsWithBehaviour(Health.class)) {
            if (e == target)
                continue;
            if (e.getTransform().getPosition().subtract(center).lengthSquared() <= r2) {
                e.getBehaviour(Health.class).damage(damage);
            }
        }

        world.instantiate(() -> {
            GameObject fx = new GameObject();
            fx.addBehaviour(new ExplosionVisual(radius, 0.3));
            return fx;
        }, center);
    }
}
