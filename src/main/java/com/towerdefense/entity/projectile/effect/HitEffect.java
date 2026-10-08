package com.towerdefense.entity.projectile.effect;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.World;

public interface HitEffect {
    void apply(GameObject target, GameObject projectile, World world);
}
