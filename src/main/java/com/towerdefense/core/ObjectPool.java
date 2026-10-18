package com.towerdefense.core;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Object Pool cho {@link GameObject}: tái sử dụng object thay vì tạo mới.
 * Object lấy từ pool tự trả về khi bị huỷ ({@link ReturnToPool}).
 */
public class ObjectPool {
    private static final AtomicLong totalCreated = new AtomicLong();
    private static final AtomicLong totalReused = new AtomicLong();
    private static final Set<ObjectPool> registeredPools =
            Collections.newSetFromMap(new WeakHashMap<>());

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
        synchronized (registeredPools) {
            registeredPools.add(this);
        }
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

    /**
     * Lấy object từ pool. Khi {@link PoolSettings} đang tắt, tạo object mới không gắn với pool
     * (không được đếm và không tự trả về).
     */
    public GameObject obtain(Vector2 position) {
        if (!PoolSettings.isEnabled()) {
            GameObject unpooled = factory.get();
            totalCreated.incrementAndGet();
            unpooled.getTransform().setPosition(position);
            return unpooled;
        }
        GameObject obj = free.poll();
        if (obj != null) {
            freeSet.remove(obj);
            reused++;
            totalReused.incrementAndGet();
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
        totalCreated.incrementAndGet();
        return obj;
    }

    private void release(GameObject obj) {
        if (freeSet.contains(obj)) {
            return; // đã nằm trong pool, bỏ qua lần trả thứ hai
        }
        if (!PoolSettings.isEnabled() || free.size() >= maxSize) {
            discarded++; // pool đang tắt hoặc đã đầy: không giữ lại
            return;
        }
        free.push(obj);
        freeSet.add(obj);
    }

    private void clear() {
        discarded += free.size();
        free.clear();
        freeSet.clear();
    }

    static void clearAll() {
        synchronized (registeredPools) {
            for (ObjectPool pool : registeredPools) {
                pool.clear();
            }
        }
    }

    /** Tổng số object đã tạo bằng factory. */
    public int getCreatedCount() { return created; }

    /** Số lần {@link #obtain} lấy được object cũ. */
    public int getReusedCount() { return reused; }

    /** Số object đang rảnh trong pool. */
    public int getFreeCount() { return free.size(); }

    /** Số object đang được sử dụng (đã lấy, chưa trả, chưa bị bỏ). */
    public int getActiveCount() { return created - free.size() - discarded; }

    /** Tổng số object mới mà tất cả pool đã tạo, kể cả khi pooling bị tắt. */
    public static long getTotalCreatedCount() { return totalCreated.get(); }

    /** Tổng số lần tất cả pool tái sử dụng object. */
    public static long getTotalReusedCount() { return totalReused.get(); }

    /** Bắt đầu lại cửa sổ thống kê dùng cho debug UI. */
    public static void resetTotalCounts() {
        totalCreated.set(0);
        totalReused.set(0);
    }

    private static class ReturnToPool extends Behaviour {
        private final ObjectPool pool;

        ReturnToPool(ObjectPool pool) { this.pool = pool; }

        @Override
        public void onDestroy() { pool.release(gameObject); }
    }
}
