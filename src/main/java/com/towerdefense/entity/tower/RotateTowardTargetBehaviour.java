package com.towerdefense.entity.tower;

import com.towerdefense.behaviour.Targeting;
import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;

public class RotateTowardTargetBehaviour extends Behaviour {

    private final Targeting targeting;
    private final double rotationSpeed;

    public RotateTowardTargetBehaviour(Targeting targeting, double rotationSpeed) {
        this.targeting = targeting;
        this.rotationSpeed = rotationSpeed;
    }

    @Override
    public void update(double deltaTime) {
        GameObject target = targeting.getCurrentTarget();
        if (target == null)
            return;

        Vector2 position = gameObject.getTransform().getPosition();
        Vector2 targetPosition = target.getTransform().getPosition();
        Vector2 direction = targetPosition.subtract(position);

        double targetAngle = Math.toDegrees(Math.atan2(direction.y(), direction.x()));
        double currentAngle = gameObject.getTransform().getRotation();
        double difference = normalizeAngle(targetAngle - currentAngle);
        double maxRotation = rotationSpeed * deltaTime;

        if (Math.abs(difference) <= maxRotation) {
            currentAngle = targetAngle;
        } else {
            currentAngle += Math.signum(difference) * maxRotation;
        }

        gameObject.getTransform().setRotation(currentAngle);
    }

    private double normalizeAngle(double angle) {
        while (angle > 180)
            angle -= 360;
        while (angle < -180)
            angle += 360;
        return angle;
    }
}