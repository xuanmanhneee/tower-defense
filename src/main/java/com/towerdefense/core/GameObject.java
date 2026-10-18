package com.towerdefense.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameObject {

    private final Transform transform = new Transform();
    private final List<Behaviour> behaviours = new ArrayList<>();

    private final List<Behaviour> behavioursView = Collections.unmodifiableList(behaviours);

    private Scene scene;
    private boolean alive = true;

    public Transform getTransform() {
        return transform;
    }

    void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public <T extends Behaviour> T addBehaviour(T behaviour) {
        behaviour.attach(this);
        behaviours.add(behaviour);
        return behaviour;
    }

    public <T extends Behaviour> T getBehaviour(Class<T> type) {
        for (Behaviour behaviour : behaviours) {
            if (type.isInstance(behaviour)) {
                return type.cast(behaviour);
            }
        }

        return null;
    }

    public List<Behaviour> getBehaviours() {
        return behavioursView;
    }

    public void start() {
        for (int i = 0; i < behaviours.size(); i++) {
            behaviours.get(i).start();
        }
    }

    public void update(double deltaTime) {
        for (int i = 0; i < behaviours.size(); i++) {
            behaviours.get(i).update(deltaTime);
        }
    }

    public void destroy() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void notifyDestroyed() {
        for (int i = 0; i < behaviours.size(); i++) {
            behaviours.get(i).onDestroy();
        }
    }

    public void reset(Vector2 position) {
        this.alive = true;
        this.transform.setPosition(position);
        this.transform.setRotation(0);
        for (int i = 0; i < behaviours.size(); i++) {
            behaviours.get(i).onReset();
        }
    }
}
