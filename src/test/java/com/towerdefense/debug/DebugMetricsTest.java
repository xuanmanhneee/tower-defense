package com.towerdefense.debug;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.ObjectPool;
import com.towerdefense.core.PoolSettings;
import com.towerdefense.core.Vector2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DebugMetricsTest {

    private final DebugMetrics metrics = DebugMetrics.getInstance();

    @BeforeEach
    void setUp() {
        PoolSettings.setEnabled(true);
        metrics.resetObjectCounters();
    }

    @AfterEach
    void restoreDefaults() {
        PoolSettings.setEnabled(true);
        metrics.resetObjectCounters();
    }

    @Test
    void toggleControlsRuntimePoolSettingAndResetsCounters() {
        ObjectPool pool = new ObjectPool(GameObject::new);
        pool.obtain(Vector2.ZERO);
        assertEquals(1, metrics.getCreatedObjectCount());

        metrics.toggleObjectPool();

        assertFalse(PoolSettings.isEnabled());
        assertFalse(metrics.isObjectPoolEnabled());
        assertEquals(0, metrics.getCreatedObjectCount());
        assertEquals(0, metrics.getReusedObjectCount());
    }

    @Test
    void reportsCreatedAndReusedObjectsAcrossPools() {
        ObjectPool first = new ObjectPool(GameObject::new);
        ObjectPool second = new ObjectPool(GameObject::new);

        GameObject reusable = first.obtain(Vector2.ZERO);
        reusable.notifyDestroyed();
        first.obtain(Vector2.ZERO);
        second.obtain(Vector2.ZERO);

        assertEquals(2, metrics.getCreatedObjectCount());
        assertEquals(1, metrics.getReusedObjectCount());
        assertTrue(metrics.isObjectPoolEnabled());
    }

    @Test
    void countsNewObjectsWhilePoolingIsDisabled() {
        metrics.toggleObjectPool();
        ObjectPool pool = new ObjectPool(GameObject::new);

        pool.obtain(Vector2.ZERO);
        pool.obtain(Vector2.ZERO);

        assertEquals(2, metrics.getCreatedObjectCount());
        assertEquals(0, metrics.getReusedObjectCount());
    }
}
