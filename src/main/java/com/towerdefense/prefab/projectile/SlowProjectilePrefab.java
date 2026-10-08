package com.towerdefense.prefab.projectile;

import com.towerdefense.entity.projectile.effect.HitEffect;
import com.towerdefense.entity.projectile.effect.DamageOnHit;
import com.towerdefense.entity.projectile.effect.SlowOnHit;

import javafx.scene.paint.Color;

public class SlowProjectilePrefab extends ProjectilePrefab {
    @Override protected Color color() { return Color.web("#4dd0e1"); }
    @Override protected double speed() { return 450; }
    @Override protected HitEffect hitEffect() {
        return new SlowOnHit(new DamageOnHit(6), 0.5, 2.0);   // còn 50% tốc độ trong 2s
    }
}
