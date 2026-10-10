package com.towerdefense.entity.projectile.effect;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;

/** Hiệu ứng vòng nổ: một object chỉ chứa {@link ExplosionVisual}, tự huỷ khi hết thời gian. */
public class ExplosionPrefab implements Prefab {
    private final double radius;
    private final double duration;

    public ExplosionPrefab(double radius, double duration) {
        this.radius = radius;
        this.duration = duration;
    }

    @Override
    public GameObject instantiate() {
        GameObject fx = new GameObject();
        fx.addBehaviour(new ExplosionVisual(radius, duration));
        return fx;
    }
}
