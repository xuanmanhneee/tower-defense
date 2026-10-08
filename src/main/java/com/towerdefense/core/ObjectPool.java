package com.towerdefense.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

public class ObjectPool {
    private final Supplier<GameObject> factory;
    private final Deque<GameObject> free = new ArrayDeque<>();

    public ObjectPool(Supplier<GameObject> factory) { this.factory = factory; }

    public GameObject obtain(Vector2 position) {
        GameObject obj = free.poll();
        if (obj != null) {
            obj.reset(position);
            return obj;
        }
        obj = factory.get();
        obj.getTransform().setPosition(position);
        obj.addBehaviour(new ReturnToPool(this));
        return obj;
    }

    private void release(GameObject obj) { free.push(obj); }

    private static class ReturnToPool extends Behaviour {
        private final ObjectPool pool;
        ReturnToPool(ObjectPool pool) { this.pool = pool; }
        @Override public void onDestroy() { pool.release(gameObject); }
    }
}