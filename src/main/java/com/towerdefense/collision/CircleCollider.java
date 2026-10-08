package com.towerdefense.collision;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.Vector2;

public class CircleCollider extends Behaviour {

    private final double radius;
    private final CollisionLayer layer;
    private final Set<CollisionLayer> mask = EnumSet.noneOf(CollisionLayer.class);

    public CircleCollider(double radius, CollisionLayer layer) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be greater than zero");
        }

        this.radius = radius;
        this.layer = Objects.requireNonNull(layer, "layer must not be null");
    }

    public CircleCollider detects(CollisionLayer... layers) {
        Collections.addAll(mask, layers);
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public Vector2 getCenter() {
        return gameObject.getTransform().getPosition();
    }

    public boolean interactsWith(CircleCollider other) {
        return mask.contains(other.layer);
    }

    public boolean overlaps(CircleCollider other) {
        Vector2 a = getCenter(), b = other.getCenter();
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        double r = radius + other.radius;
        return dx * dx + dy * dy <= r * r;
    }
}