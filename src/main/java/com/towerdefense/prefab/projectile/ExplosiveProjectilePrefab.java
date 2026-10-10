package com.towerdefense.prefab.projectile;

import com.towerdefense.core.PooledPrefab;
import com.towerdefense.entity.projectile.effect.DamageOnHit;
import com.towerdefense.entity.projectile.effect.ExplodeOnHit;
import com.towerdefense.entity.projectile.effect.ExplosionPrefab;
import com.towerdefense.entity.projectile.effect.HitEffect;

import javafx.scene.paint.Color;

public class ExplosiveProjectilePrefab extends ProjectilePrefab {

    private static final double EXPLOSION_RADIUS = 128;
    private static final double EXPLOSION_DURATION = 0.3;

    // dùng chung cho mọi viên đạn nổ: giữ pool lâu dài thay vì tạo mới mỗi lần
    private static final PooledPrefab EXPLOSION_FX = new PooledPrefab(
            new ExplosionPrefab(EXPLOSION_RADIUS, EXPLOSION_DURATION), 8);

    @Override
    protected Color color() {
        return Color.web("#ff7043");
    }

    @Override
    protected double speed() {
        return 380; // chậm hơn đạn thường cho cân bằng
    }

    @Override
    protected HitEffect hitEffect() {
        // 1. DamageOnHit gây sát thương trực tiếp lên mục tiêu
        // 2. ExplodeOnHit gây sát thương lan lên địch xung quanh + spawn vòng nổ
        return new ExplodeOnHit(new DamageOnHit(15), EXPLOSION_RADIUS, 8, EXPLOSION_FX);
    }
}