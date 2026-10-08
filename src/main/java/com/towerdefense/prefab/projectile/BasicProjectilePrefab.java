package com.towerdefense.prefab.projectile;

import com.towerdefense.entity.projectile.effect.DamageOnHit;
import com.towerdefense.entity.projectile.effect.HitEffect;

import javafx.scene.paint.Color;

public class BasicProjectilePrefab extends ProjectilePrefab {
    private final double damage;

    public BasicProjectilePrefab() {
        this(10);
    }

    public BasicProjectilePrefab(double damage) {
        this.damage = damage;
    }

    @Override
    protected Color color() {
        return Color.web("#ffd54f");
    }

    @Override
    protected double speed() {
        return 500;
    }

    @Override
    protected HitEffect hitEffect() {
        return new DamageOnHit(damage);
    }
}