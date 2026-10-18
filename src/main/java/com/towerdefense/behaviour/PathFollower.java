package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.Transform;
import com.towerdefense.core.Vector2;

import java.util.List;

public class PathFollower extends Behaviour {

    private final double speed;
    private List<Vector2> waypoints = List.of();

    private SpeedModifiers speedModifiers;

    private int currentIndex = 1;

    public PathFollower(double speed) {
        this.speed = speed;
    }

    @Override
    public void start() {
        speedModifiers = gameObject.getBehaviour(SpeedModifiers.class);
    }

    /** Quay về đầu đường đi; danh sách waypoint được giữ nguyên. */
    @Override
    public void onReset() {
        currentIndex = 1;
    }

    @Override
    public void update(double deltaTime) {

        if (currentIndex >= waypoints.size()) {
            return;
        }

        Transform transform = gameObject.getTransform();

        Vector2 position = transform.getPosition();
        Vector2 target = waypoints.get(currentIndex);

        double distance = position.distanceTo(target);

        double multiplier = speedModifiers != null ? speedModifiers.getMultiplier() : 1.0;

        double step = speed * multiplier * deltaTime;

        if (distance <= step) {
            transform.setPosition(target);
            currentIndex++;
            return;
        }

        Vector2 direction = target
                .subtract(position)
                .normalized();

        transform.translate(
                direction.multiply(step));
    }

    public void setWaypoints(List<Vector2> waypoints) {
        if (waypoints.isEmpty()) {
            throw new IllegalArgumentException("Waypoints cannot be empty");
        }
        this.waypoints = waypoints;
        this.currentIndex = 1;
    }
}