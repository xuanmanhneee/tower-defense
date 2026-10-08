package com.towerdefense.prefab.projectile;

import com.towerdefense.entity.projectile.effect.DamageOnHit;
import com.towerdefense.entity.projectile.effect.ExplodeOnHit;
import com.towerdefense.entity.projectile.effect.HitEffect;

import javafx.scene.paint.Color;

public class ExplosiveProjectilePrefab extends ProjectilePrefab {

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
        return new ExplodeOnHit(new DamageOnHit(15), 128, 8);
    }
}