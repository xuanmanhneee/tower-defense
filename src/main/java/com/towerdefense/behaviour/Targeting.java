package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.TargetProvider;
import com.towerdefense.core.Vector2;

public class Targeting extends Behaviour {

    private TargetProvider targetProvider;
    private GameObject currentTarget;

    private final double range;

    public Targeting(double range) {
        this.range = range;
    }

    @Override
    public void start(){
        targetProvider = gameObject.getScene();
    }

    @Override
    public void update(double deltaTime) {
        currentTarget = findClosestEnemy();
    }

    private GameObject findClosestEnemy() {
        Vector2 position = gameObject.getTransform().getPosition();
        GameObject closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (GameObject obj : targetProvider.findObjectsWithBehaviour(PathFollower.class)) {
            double distance = position.distanceTo(obj.getTransform().getPosition());
            if (distance <= range && distance < closestDistance) {
                closest = obj;
                closestDistance = distance;
            }
        }
        return closest;
    }

    public GameObject getCurrentTarget() {
        return currentTarget;
    }
}