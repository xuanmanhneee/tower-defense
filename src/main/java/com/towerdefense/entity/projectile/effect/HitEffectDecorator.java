package com.towerdefense.entity.projectile.effect;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.World;

public abstract class HitEffectDecorator implements HitEffect {
    protected final HitEffect inner;
    protected HitEffectDecorator(HitEffect inner) { this.inner = inner; }

    @Override
    public void apply(GameObject target, GameObject projectile, World world) {
        inner.apply(target, projectile, world);   // chuyển tiếp, lớp con thêm việc của mình
    }
}
