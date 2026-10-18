package com.towerdefense.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PoolSettingsTest {

    private ObjectPool pool;
    private Scene scene;
    private Prefab pooled;

    @BeforeEach
    void setUp() {
        PoolSettings.setEnabled(true);
        pool = new ObjectPool(GameObject::new);
        scene = new Scene();
        pooled = () -> pool.obtain(Vector2.ZERO);
    }

    @AfterEach
    void restoreDefault() {
        PoolSettings.setEnabled(true);
    }

    private GameObject spawn() {
        GameObject o = scene.instantiate(pooled, Vector2.ZERO);
        scene.update(0.1);
        return o;
    }

    private void destroy(GameObject o) {
        o.destroy();
        scene.update(0.1);
    }

    @Test
    void enabledByDefault() {
        assertTrue(PoolSettings.isEnabled());
    }

    @Test
    void whenDisabledEveryObtainCreatesNewObject() {
        PoolSettings.setEnabled(false);

        GameObject a = spawn();
        destroy(a);
        GameObject b = spawn();

        assertNotSame(a, b);
        assertEquals(0, pool.getCreatedCount());
        assertEquals(0, pool.getReusedCount());
        assertEquals(0, pool.getFreeCount());
    }

    @Test
    void whenDisabledObjectsAreNotReturnedToPool() {
        PoolSettings.setEnabled(false);

        GameObject o = spawn();
        destroy(o);

        assertEquals(0, pool.getFreeCount());
    }

    @Test
    void objectObtainedWhileEnabledIsDiscardedIfDestroyedWhileDisabled() {
        GameObject o = spawn();
        PoolSettings.setEnabled(false);

        destroy(o);

        assertEquals(0, pool.getFreeCount());
        assertEquals(0, pool.getActiveCount());
    }

    @Test
    void reenablingResumesReuse() {
        PoolSettings.setEnabled(false);
        destroy(spawn());

        PoolSettings.setEnabled(true);
        GameObject first = spawn();
        destroy(first);
        GameObject second = spawn();

        assertSame(first, second);
        assertEquals(1, pool.getCreatedCount());
        assertEquals(1, pool.getReusedCount());
    }

    @Test
    void disablingClearsFreeObjectsAndReenableStartsFresh() {
        GameObject a = spawn();
        destroy(a);
        assertEquals(1, pool.getFreeCount());

        PoolSettings.setEnabled(false);
        assertEquals(0, pool.getFreeCount());

        GameObject b = spawn();
        assertNotSame(a, b);

        PoolSettings.setEnabled(true);
        GameObject c = spawn();

        assertNotSame(a, c);
        assertNotSame(b, c);
        assertEquals(2, pool.getCreatedCount());
        assertEquals(0, pool.getReusedCount());
    }
}
