package com.towerdefense.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scene implements World {

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> pendingAdditions = new ArrayList<>();

    private final List<GameObject> gameObjectsView = Collections.unmodifiableList(gameObjects);
    private final List<GameObject> dead = new ArrayList<>();

    private final EventBus eventBus = new EventBus();

    private void addGameObject(GameObject gameObject) {
        pendingAdditions.add(gameObject);
    }

    @Override
    public List<GameObject> findObjectsWithBehaviour(Class<? extends Behaviour> type) {
        List<GameObject> result = new ArrayList<>();
        for (GameObject obj : gameObjects) {
            if (obj.isAlive() && obj.getBehaviour(type) != null) {
                result.add(obj);
            }
        }
        return result;
    }

    public List<GameObject> getGameObjects() {
        return gameObjectsView;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void update(double deltaTime) {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject o = gameObjects.get(i);
            if (o.isAlive())
                o.update(deltaTime);
        }
        sweepDead();
        flushPending();
    }

    public GameObject instantiate(Prefab prefab) {
        GameObject o = prefab.instantiate();
        addGameObject(o);
        return o;
    }

    public GameObject instantiate(Prefab prefab, Vector2 position) {
        GameObject o = instantiate(prefab);
        o.getTransform().setPosition(position);
        return o;
    }

    private void flushPending() {
        for (int i = 0; i < pendingAdditions.size(); i++) {
            GameObject o = pendingAdditions.get(i);
            o.setScene(this);
            gameObjects.add(o);
            o.start();
        }
        pendingAdditions.clear();
    }

    private void sweepDead() {
        dead.clear();
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject o = gameObjects.get(i);
            if (!o.isAlive())
                dead.add(o);
        }
        if (dead.isEmpty())
            return;

        gameObjects.removeIf(o -> !o.isAlive()); // gỡ trước
        for (int i = 0; i < dead.size(); i++) {
            dead.get(i).notifyDestroyed(); // thông báo sau
        }
    }
}