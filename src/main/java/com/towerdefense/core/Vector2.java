package com.towerdefense.core;

public record Vector2(double x, double y) {

    public static final Vector2 ZERO = new Vector2(0, 0);

    private static final double EPSILON = 1e-9;

    public Vector2 add(Vector2 other) {
        return new Vector2(
                x + other.x,
                y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(
                x - other.x,
                y - other.y);
    }

    public Vector2 multiply(double scalar) {
        return new Vector2(
                x * scalar,
                y * scalar);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double distanceTo(Vector2 other) {
        return subtract(other).length();
    }

    public Vector2 normalized() {
        double len = length();
        if (len < EPSILON) {
            return ZERO;
        }
        return multiply(1.0 / len);
    }

    public double lengthSquared() {
        return x * x + y * y;
    }
}