package com.towerdefense.core;

public abstract class Behaviour {

    protected GameObject gameObject;

    public final GameObject getGameObject() {
        return gameObject;
    }

    void attach(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public void start() {
    }

    public void update(double deltaTime) {
    }

    public void onDestroy() {
    }

    public void onReset() {
    }
}