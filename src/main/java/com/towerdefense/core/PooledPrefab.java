package com.towerdefense.core;

/**
 * Decorator cho {@link Prefab}: mỗi lần {@link #instantiate()} lấy object từ
 * {@link ObjectPool} thay vì tạo mới. Dùng được ở mọi nơi nhận {@code Prefab}
 * (vd {@code Scene.instantiate(prefab, position)}), vị trí được đặt sau đó.
 *
 * Phải giữ một instance lâu dài (dùng chung), nếu tạo mới mỗi lần thì pool vô tác dụng.
 */
public class PooledPrefab implements Prefab {
    private final ObjectPool pool;

    public PooledPrefab(Prefab source) {
        this(source, 0);
    }

    public PooledPrefab(Prefab source, int prewarm) {
        this.pool = new ObjectPool(source::instantiate);
        pool.prewarm(prewarm);
    }

    @Override
    public GameObject instantiate() {
        return pool.obtain(Vector2.ZERO);
    }

    public ObjectPool getPool() {
        return pool;
    }
}
