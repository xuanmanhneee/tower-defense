package com.towerdefense.core;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Object Pool cho {@link GameObject}: tái sử dụng object thay vì tạo mới.
 * Object lấy từ pool tự trả về khi bị huỷ ({@link ReturnToPool}).
 */
public class ObjectPool {
    private final Supplier<GameObject> factory;
    private final int maxSize;
    private final Deque<GameObject> free = new ArrayDeque<>();
    private final Set<GameObject> freeSet = Collections.newSetFromMap(new IdentityHashMap<>());

    private int created;
    private int reused;
    private int discarded;

    public ObjectPool(Supplier<GameObject> factory) {
        this(factory, Integer.MAX_VALUE);
    }

    /** @param maxSize số object rảnh tối đa giữ lại; object trả về khi đã đầy bị bỏ. */
    public ObjectPool(Supplier<GameObject> factory, int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than zero");
        }
        this.factory = factory;
        this.maxSize = maxSize;
    }

    /** Tạo sẵn tối đa {@code count} object rảnh (không vượt maxSize). */
    public void prewarm(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must not be negative");
        }
        while (count-- > 0 && free.size() < maxSize) {
            GameObject obj = create(Vector2.ZERO);
            free.push(obj);
            freeSet.add(obj);
        }
    }

    public GameObject obtain(Vector2 position) {
        GameObject obj = free.poll();
        if (obj != null) {
            freeSet.remove(obj);
            reused++;
            obj.reset(position);
            return obj;
        }
        return create(position);
    }

    private GameObject create(Vector2 position) {
        GameObject obj = factory.get();
        obj.getTransform().setPosition(position);
        obj.addBehaviour(new ReturnToPool(this)); // luôn là Behaviour cuối cùng
        created++;
        return obj;
    }

    private void release(GameObject obj) {
        if (freeSet.contains(obj)) {
            return; // đã nằm trong pool, bỏ qua lần trả thứ hai
        }
        if (free.size() >= maxSize) {
            discarded++;
            return;
        }
        free.push(obj);
        freeSet.add(obj);
    }

    /** Tổng số object đã tạo bằng factory. */
    public int getCreatedCount() { return created; }

    /** Số lần {@link #obtain} lấy được object cũ. */
    public int getReusedCount() { return reused; }

    /** Số object đang rảnh trong pool. */
    public int getFreeCount() { return free.size(); }

    /** Số object đang được sử dụng (đã lấy, chưa trả, chưa bị bỏ). */
    public int getActiveCount() { return created - free.size() - discarded; }

    private static class ReturnToPool extends Behaviour {
        private final ObjectPool pool;

        ReturnToPool(ObjectPool pool) { this.pool = pool; }

        @Override
        public void onDestroy() { pool.release(gameObject); }
    }
}
