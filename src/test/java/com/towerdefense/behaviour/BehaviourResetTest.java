package com.towerdefense.behaviour;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.Scene;
import com.towerdefense.core.Vector2;
import java.util.List;
import org.junit.jupiter.api.Test;

class BehaviourResetTest {

    @Test
    void healthIsRestored() {
        GameObject o = new GameObject();
        Health h = o.addBehaviour(new Health(100));
        h.damage(60);

        o.reset(Vector2.ZERO);

        assertEquals(100, h.getHealth());
    }

    @Test
    void healthKeepsOnDeathCallback() {
        GameObject o = new GameObject();
        Health h = o.addBehaviour(new Health(10));
        int[] deaths = {0};
        h.setOnDeath(() -> deaths[0]++);
        h.damage(10);

        o.reset(Vector2.ZERO);
        h.damage(10);

        assertEquals(2, deaths[0]);
    }

    @Test
    void pathFollowerRestartsFromFirstSegment() {
        GameObject o = new GameObject();
        PathFollower pf = o.addBehaviour(new PathFollower(100));
        pf.setWaypoints(List.of(new Vector2(0, 0), new Vector2(10, 0), new Vector2(10, 10)));
        pf.update(1); // tới waypoint 1
        pf.update(1); // tới waypoint 2
        assertEquals(new Vector2(10, 10), o.getTransform().getPosition());

        o.reset(Vector2.ZERO);
        pf.update(0.05); // 5px dọc theo đoạn đầu

        assertEquals(5, o.getTransform().getPosition().x(), 1e-9);
        assertEquals(0, o.getTransform().getPosition().y(), 1e-9);
    }

    @Test
    void autoDestroyRestartsTimer() {
        GameObject o = new GameObject();
        AutoDestroy ad = o.addBehaviour(new AutoDestroy(2));
        ad.update(1.5);

        o.reset(Vector2.ZERO);
        ad.update(1.5);

        assertTrue(o.isAlive());
    }

    @Test
    void fireControlCooldownIsCleared() {
        Scene scene = new Scene();
        int[] shots = {0};
        GameObject target = new GameObject();
        target.getTransform().setPosition(new Vector2(10, 0));
        target.addBehaviour(new PathFollower(1));
        scene.instantiate(() -> target);

        GameObject tower = new GameObject();
        Targeting targeting = tower.addBehaviour(new Targeting(100));
        FireControl fc = tower.addBehaviour(new FireControl(targeting, (origin, dir, t, w) -> shots[0]++, 10));
        scene.instantiate(() -> tower);
        scene.update(0.1); // đưa vào Scene
        scene.update(0.1); // Targeting tìm mục tiêu, FireControl bắn -> cooldown 10s
        assertEquals(1, shots[0]);

        tower.reset(Vector2.ZERO);
        scene.update(0.1);

        assertEquals(2, shots[0]);
    }

    @Test
    void targetingForgetsCurrentTarget() {
        Scene scene = new Scene();
        GameObject enemy = new GameObject();
        enemy.getTransform().setPosition(new Vector2(10, 0));
        enemy.addBehaviour(new PathFollower(1));
        scene.instantiate(() -> enemy);

        GameObject tower = new GameObject();
        Targeting targeting = tower.addBehaviour(new Targeting(100));
        scene.instantiate(() -> tower);
        scene.update(0.1);
        scene.update(0.1);
        assertEquals(enemy, targeting.getCurrentTarget());

        tower.reset(Vector2.ZERO);

        assertNull(targeting.getCurrentTarget());
    }
}
