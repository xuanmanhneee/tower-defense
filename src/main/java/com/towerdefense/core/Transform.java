package com.towerdefense.core;

public class Transform {

    private Vector2 position;
    private double rotation;

    public Transform() {
        this(Vector2.ZERO);
    }

    public Transform(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public double getRotation() { 
        return rotation; 
    } 
    
    public void setRotation(double rotation) { 
        this.rotation = rotation; 
    }

    public void translate(Vector2 offset) {
        position = position.add(offset);
    }
}