package com.towerdefense.entity.projectile;

import com.towerdefense.behaviour.Health;
import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Transform;
import com.towerdefense.core.Vector2;

public class StraightProjectileMovement extends Behaviour {

    private GameObject target;
    private double speed;
    private double damage;
    private Vector2 direction;

    public StraightProjectileMovement() {
    }

    public StraightProjectileMovement(Vector2 direction, GameObject target, double speed, double damage) {
        this.direction = direction;
        this.target = target;
        this.speed = speed;
        this.damage = damage;
    }

    public void setup(Vector2 direction, GameObject target, double speed, double damage) {
        this.direction = direction;
        this.target = target;
        this.speed = speed;
        this.damage = damage;
    }

    @Override
    public void update(double deltaTime) {
        if (direction == null) {
            gameObject.destroy();
            return;
        }

        Transform transform = gameObject.getTransform();
        double step = speed * deltaTime;
        transform.translate(direction.multiply(step));

        if (target != null && target.isAlive()) {
            double distance = transform.getPosition().distanceTo(target.getTransform().getPosition());
            if (distance <= step) {
                applyDamage();
                gameObject.destroy();
            }
        }
    }

    private void applyDamage() {
        Health health = target.getBehaviour(Health.class);
        if (health != null) {
            health.damage(damage);
        }
    }
}