package com.towerdefense.entity.projectile;

import com.towerdefense.behaviour.Health;
import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Transform;
import com.towerdefense.core.Vector2;

public class HomingProjectileMovement extends Behaviour {

    private GameObject target;
    private double speed;
    private double damage;

    public HomingProjectileMovement() {
    }

    public HomingProjectileMovement(GameObject target, double speed, double damage) {
        this.target = target;
        this.speed = speed;
        this.damage = damage;
    }

    public void setup(GameObject target, double speed, double damage) {
        this.target = target;
        this.speed = speed;
        this.damage = damage;
    }

    @Override
    public void update(double deltaTime) {
        if (target == null || !target.isAlive()) {
            gameObject.destroy();
            return;
        }

        Transform transform = gameObject.getTransform();
        Vector2 position = transform.getPosition();
        Vector2 targetPosition = target.getTransform().getPosition();

        double distance = position.distanceTo(targetPosition);
        double step = speed * deltaTime;

        if (distance <= step) {
            Health health = target.getBehaviour(Health.class);
            if (health != null) {
                health.damage(damage);
            }
            gameObject.destroy();
            return;
        }

        Vector2 direction = targetPosition.subtract(position).normalized();
        transform.translate(direction.multiply(step));
    }
}