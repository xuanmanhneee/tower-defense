package com.towerdefense.entity.projectile.effect;

import com.towerdefense.behaviour.SpeedModifiers;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.World;

public class SlowOnHit extends HitEffectDecorator {
    private final double multiplier, duration;

    public SlowOnHit(HitEffect inner, double factor, double duration) {
        super(inner);
        this.multiplier = factor;
        this.duration = duration;
    }

    @Override
    public void apply(GameObject target, GameObject projectile, World world) {
        super.apply(target, projectile, world);
        SpeedModifiers sm = target.getBehaviour(SpeedModifiers.class);
        if (sm != null)
            sm.add(multiplier, duration);
    }
}
