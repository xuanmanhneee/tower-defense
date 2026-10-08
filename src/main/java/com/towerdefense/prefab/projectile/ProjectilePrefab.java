package com.towerdefense.prefab.projectile;

import com.towerdefense.behaviour.HitHandler;
import com.towerdefense.behaviour.LinearMotion;
import com.towerdefense.behaviour.TravelLimit;
import com.towerdefense.collision.CircleCollider;
import com.towerdefense.collision.CollisionLayer;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;
import com.towerdefense.entity.projectile.effect.HitEffect;
import com.towerdefense.render.DiscRenderer;
import com.towerdefense.render.RenderLayer;

import javafx.scene.paint.Color;

public abstract class ProjectilePrefab implements Prefab {

    protected double radius() {
        return 4;
    }

    protected double maxRange() {
        return 180; // lớn hơn tầm bắn tháp (150) để đạn kịp trúng địch ở mép tầm
    }

    protected abstract Color color();

    protected abstract double speed();

    protected abstract HitEffect hitEffect();

@Override
public final GameObject instantiate() {
    GameObject projectile = new GameObject();
    projectile.addBehaviour(new DiscRenderer(RenderLayer.PROJECTILE, color(), radius(), true));
    projectile.addBehaviour(new CircleCollider(radius(), CollisionLayer.PROJECTILE).detects(CollisionLayer.ENEMY));
    projectile.addBehaviour(new LinearMotion(speed()));
    projectile.addBehaviour(new TravelLimit(maxRange()));
    projectile.addBehaviour(new HitHandler(hitEffect()));
    return projectile;
}
}