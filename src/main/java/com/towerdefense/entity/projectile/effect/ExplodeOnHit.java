package com.towerdefense.entity.projectile.effect;

import com.towerdefense.behaviour.Health;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;
import com.towerdefense.core.Vector2;
import com.towerdefense.core.World;

public class ExplodeOnHit extends HitEffectDecorator {
    private static final double VISUAL_DURATION = 0.3;

    private final double radius, damage;
    private final Prefab explosionFx;

    public ExplodeOnHit(HitEffect inner, double radius, double damage) {
        this(inner, radius, damage, new ExplosionPrefab(radius, VISUAL_DURATION));
    }

    /** @param explosionFx prefab hiệu ứng vòng nổ; truyền prefab có pool để tái sử dụng. */
    public ExplodeOnHit(HitEffect inner, double radius, double damage, Prefab explosionFx) {
        super(inner);
        this.radius = radius;
        this.damage = damage;
        this.explosionFx = explosionFx;
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

        world.instantiate(explosionFx, center);
    }
}
