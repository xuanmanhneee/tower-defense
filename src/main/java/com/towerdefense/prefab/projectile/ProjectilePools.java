package com.towerdefense.prefab.projectile;

import com.towerdefense.core.PooledPrefab;

/**
 * Các prefab đạn dùng chung, mỗi loại một pool: mọi tháp cùng loại bắn ra từ cùng pool.
 */
public final class ProjectilePools {
    private ProjectilePools() {
    }

    private static final int PREWARM = 16;

    public static final PooledPrefab BASIC = new PooledPrefab(new BasicProjectilePrefab(10), PREWARM);
    public static final PooledPrefab BASIC_WEAK = new PooledPrefab(new BasicProjectilePrefab(3), PREWARM);
    public static final PooledPrefab SLOW = new PooledPrefab(new SlowProjectilePrefab(), PREWARM);
    public static final PooledPrefab EXPLOSIVE = new PooledPrefab(new ExplosiveProjectilePrefab(), PREWARM);
}
