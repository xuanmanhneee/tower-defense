package com.towerdefense.debug;

import com.towerdefense.core.ObjectPool;
import com.towerdefense.core.PoolSettings;

/**
 * Adapter between the debug UI and the object-pool runtime state.
 */
public final class DebugMetrics {

    private static final DebugMetrics INSTANCE = new DebugMetrics();

    private DebugMetrics() {
    }

    public static DebugMetrics getInstance() {
        return INSTANCE;
    }

    public boolean isObjectPoolEnabled() {
        return PoolSettings.isEnabled();
    }

    public void toggleObjectPool() {
        PoolSettings.setEnabled(!PoolSettings.isEnabled());
        resetObjectCounters();
    }

    public long getCreatedObjectCount() {
        return ObjectPool.getTotalCreatedCount();
    }

    public long getReusedObjectCount() {
        return ObjectPool.getTotalReusedCount();
    }

    public void resetObjectCounters() {
        ObjectPool.resetTotalCounts();
    }
}
