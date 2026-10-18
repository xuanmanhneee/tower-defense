package com.towerdefense.prefab.projectile;

import com.towerdefense.core.PooledPrefab;

/**
 * Các prefab đạn dùng chung, mỗi loại một pool: mọi tháp cùng loại bắn ra từ cùng pool.
 * Số liệu sát thương đặt ở đây cho các đạn Basic; các đạn khác tự mang số liệu trong prefab.
 */
public final class ProjectilePools {
    private ProjectilePools() {
    }

    private static final int PREWARM = 16;

    public static final PooledPrefab BASIC = new PooledPrefab(new BasicProjectilePrefab(20), PREWARM);
    public static final PooledPrefab SPREAD_MAIN = new PooledPrefab(new BasicProjectilePrefab(14), PREWARM);
    public static final PooledPrefab SPREAD_SIDE = new PooledPrefab(new BasicProjectilePrefab(7), PREWARM);
    public static final PooledPrefab SLOW = new PooledPrefab(new SlowProjectilePrefab(), PREWARM);
    public static final PooledPrefab EXPLOSIVE = new PooledPrefab(new ExplosiveProjectilePrefab(), PREWARM);
}
