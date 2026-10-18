package com.towerdefense.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.towerdefense.behaviour.Health;
import com.towerdefense.behaviour.Launchable;
import com.towerdefense.entity.projectile.effect.DamageOnHit;
import com.towerdefense.entity.projectile.effect.ExplodeOnHit;
import com.towerdefense.entity.projectile.effect.ExplosionPrefab;
import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.entity.tower.fire.SingleShotStrategy;
import com.towerdefense.prefab.projectile.BasicProjectilePrefab;
import org.junit.jupiter.api.Test;

class PooledPrefabIntegrationTest {

    @Test
    void repeatedShotsReuseProjectilesInsteadOfCreatingNew() {
        Scene scene = new Scene();
        PooledPrefab bullets = new PooledPrefab(new BasicProjectilePrefab());
        FireStrategy strategy = new SingleShotStrategy(bullets);

        int shots = 30;
        for (int i = 0; i < shots; i++) {
            strategy.fire(new Vector2(100, 100), new Vector2(1, 0), null, scene);
            for (int k = 0; k < 10; k++) {
                scene.update(0.05); // đạn bay quá maxRange (180px) rồi tự huỷ
            }
        }

        ObjectPool pool = bullets.getPool();
        assertEquals(1, pool.getCreatedCount());
        assertEquals(shots - 1, pool.getReusedCount());
        assertEquals(0, pool.getActiveCount());
    }

    @Test
    void reusedProjectileFliesAgainFromNewPosition() {
        Scene scene = new Scene();
        PooledPrefab bullets = new PooledPrefab(new BasicProjectilePrefab());

        GameObject first = scene.instantiate(bullets, new Vector2(0, 0));
        first.getBehaviour(Launchable.class).launch(new Vector2(1, 0), null);
        for (int k = 0; k < 20; k++) {
            scene.update(0.05); // bay hết tầm, tự huỷ, về pool
        }
        assertEquals(1, bullets.getPool().getFreeCount());

        GameObject second = scene.instantiate(bullets, new Vector2(10, 10));
        second.getBehaviour(Launchable.class).launch(new Vector2(0, 1), null);
        for (int k = 0; k < 4; k++) {
            scene.update(0.05);
        }

        assertTrue(second.isAlive());
        assertEquals(10, second.getTransform().getPosition().x(), 1e-9);
        assertTrue(second.getTransform().getPosition().y() > 10);
    }

    @Test
    void prewarmedPrefabCreatesObjectsUpFront() {
        PooledPrefab prefab = new PooledPrefab(new BasicProjectilePrefab(), 5);
        assertEquals(5, prefab.getPool().getFreeCount());
    }

    @Test
    void explosionVisualsAreReused() {
        Scene scene = new Scene();
        PooledPrefab fx = new PooledPrefab(new ExplosionPrefab(64, 0.3));
        ExplodeOnHit effect = new ExplodeOnHit(new DamageOnHit(1), 64, 1, fx);

        GameObject target = new GameObject();
        target.addBehaviour(new Health(1000));
        scene.instantiate(() -> target);
        GameObject projectile = new GameObject();
        scene.instantiate(() -> projectile);
        scene.update(0.01);

        for (int i = 0; i < 10; i++) {
            effect.apply(target, projectile, scene);
            for (int k = 0; k < 10; k++) {
                scene.update(0.05); // 0.5s > 0.3s thời lượng hiệu ứng
            }
        }

        assertEquals(1, fx.getPool().getCreatedCount());
        assertEquals(9, fx.getPool().getReusedCount());
        assertEquals(0, fx.getPool().getActiveCount());
    }
}
