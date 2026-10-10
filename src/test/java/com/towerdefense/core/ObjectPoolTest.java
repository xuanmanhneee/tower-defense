package com.towerdefense.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.towerdefense.behaviour.Health;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectPoolTest {

    private ObjectPool pool;
    private Scene scene;
    private Prefab pooled;

    @BeforeEach
    void setUp() {
        pool = new ObjectPool(() -> {
            GameObject o = new GameObject();
            o.addBehaviour(new Health(10));
            return o;
        });
        scene = new Scene();
        pooled = () -> pool.obtain(Vector2.ZERO);
    }

    private GameObject spawn(Vector2 position) {
        GameObject o = scene.instantiate(pooled, position);
        scene.update(0.1); // flush vào Scene
        return o;
    }

    private void destroyAndSweep(GameObject o) {
        o.destroy();
        scene.update(0.1); // sweepDead -> onDestroy -> trả về pool
    }

    @Test
    void obtainCreatesWhenEmpty() {
        spawn(new Vector2(1, 1));
        assertEquals(1, pool.getCreatedCount());
        assertEquals(1, pool.getActiveCount());
        assertEquals(0, pool.getFreeCount());
        assertEquals(0, pool.getReusedCount());
    }

    @Test
    void destroyedObjectReturnsToPool() {
        GameObject o = spawn(Vector2.ZERO);
        destroyAndSweep(o);
        assertEquals(1, pool.getFreeCount());
        assertEquals(0, pool.getActiveCount());
    }

    @Test
    void reusesSameInstanceAfterRelease() {
        GameObject first = spawn(new Vector2(1, 1));
        destroyAndSweep(first);

        GameObject second = spawn(new Vector2(5, 5));

        assertSame(first, second);
        assertEquals(1, pool.getCreatedCount());
        assertEquals(1, pool.getReusedCount());
        assertEquals(new Vector2(5, 5), second.getTransform().getPosition());
    }

    @Test
    void reusedObjectIsResetToInitialState() {
        GameObject o = spawn(Vector2.ZERO);
        Health health = o.getBehaviour(Health.class);
        health.damage(10); // chết -> destroy
        scene.update(0.1);
        assertTrue(health.isDead());

        GameObject again = spawn(Vector2.ZERO);

        assertSame(o, again);
        assertTrue(again.isAlive());
        assertEquals(10, health.getHealth());
    }

    @Test
    void secondReleaseOfSameObjectIsIgnored() {
        GameObject o = spawn(Vector2.ZERO);
        destroyAndSweep(o);
        o.notifyDestroyed(); // trả lần hai
        assertEquals(1, pool.getFreeCount());
    }

    @Test
    void returnToPoolIsLastBehaviour() {
        GameObject o = spawn(Vector2.ZERO);
        var behaviours = o.getBehaviours();
        assertEquals("ReturnToPool", behaviours.get(behaviours.size() - 1).getClass().getSimpleName());
    }

    @Test
    void prewarmCreatesFreeObjects() {
        pool.prewarm(3);
        assertEquals(3, pool.getFreeCount());
        assertEquals(3, pool.getCreatedCount());
        assertEquals(0, pool.getActiveCount());
    }

    @Test
    void prewarmedObjectsAreReusedBeforeCreatingNew() {
        pool.prewarm(2);
        spawn(Vector2.ZERO);
        assertEquals(2, pool.getCreatedCount());
        assertEquals(1, pool.getReusedCount());
    }

    @Test
    void maxSizeDiscardsExcessObjects() {
        ObjectPool capped = new ObjectPool(GameObject::new, 1);
        GameObject a = capped.obtain(Vector2.ZERO);
        GameObject b = capped.obtain(Vector2.ZERO);
        a.notifyDestroyed();
        b.notifyDestroyed();
        assertEquals(1, capped.getFreeCount());
        assertEquals(0, capped.getActiveCount());
    }

    @Test
    void prewarmDoesNotExceedMaxSize() {
        ObjectPool capped = new ObjectPool(GameObject::new, 2);
        capped.prewarm(10);
        assertEquals(2, capped.getFreeCount());
        assertEquals(2, capped.getCreatedCount());
    }

    @Test
    void invalidArgumentsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new ObjectPool(GameObject::new, 0));
        assertThrows(IllegalArgumentException.class, () -> pool.prewarm(-1));
    }
}
