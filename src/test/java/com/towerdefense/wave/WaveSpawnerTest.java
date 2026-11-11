package com.towerdefense.wave;

import com.towerdefense.behaviour.PathFollower;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Scene;
import com.towerdefense.core.Vector2;
import com.towerdefense.event.WavesClearedEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaveSpawnerTest {

    @Test
    void completesAfterEverySpawnedEnemyLeavesTheScene() {
        Scene scene = new Scene();
        int[] clears = {0};
        scene.getEventBus().subscribe(WavesClearedEvent.class, event -> clears[0]++);
        WaveSpawner spawner = createSpawner(scene, List.of(
                wave(0, new SpawnGroupData("grunt", 2, 1))));

        spawner.update(0);
        assertEquals(1, spawner.getActiveEnemyCount());

        spawner.update(1);
        assertEquals(WaveState.WAITING_FOR_CLEAR, spawner.getState());
        assertEquals(2, spawner.getActiveEnemyCount());

        scene.update(0);
        destroyEnemies(scene);
        scene.update(0);
        spawner.update(0);

        assertEquals(0, spawner.getActiveEnemyCount());
        assertEquals(WaveState.COMPLETED, spawner.getState());
        spawner.update(0);
        assertEquals(1, clears[0]);
    }

    @Test
    void waitsForConfiguredDelayBeforeStartingTheNextWave() {
        Scene scene = new Scene();
        WaveSpawner spawner = createSpawner(scene, List.of(
                wave(2, new SpawnGroupData("grunt", 1, 1)),
                wave(0, new SpawnGroupData("scout", 1, 1))));

        spawner.update(0);
        scene.update(0);
        destroyEnemies(scene);
        scene.update(0);
        spawner.update(0);

        assertEquals(WaveState.BETWEEN_WAVES, spawner.getState());
        assertEquals(2, spawner.getTimeUntilNextWave());

        spawner.update(1);
        assertEquals(WaveState.BETWEEN_WAVES, spawner.getState());
        assertEquals(1, spawner.getCurrentWaveNumber());

        spawner.update(1);
        assertEquals(WaveState.SPAWNING, spawner.getState());
        assertEquals(2, spawner.getCurrentWaveNumber());
    }

    private static WaveSpawner createSpawner(Scene scene, List<WaveData> waves) {
        return new WaveSpawner(
                scene,
                List.of(new Vector2(0, 0), new Vector2(10, 0)),
                waves,
                new EnemyRegistry());
    }

    private static WaveData wave(double delayAfter, SpawnGroupData... groups) {
        return new WaveData(delayAfter, List.of(groups));
    }

    private static void destroyEnemies(Scene scene) {
        for (GameObject enemy : scene.findObjectsWithBehaviour(PathFollower.class)) {
            enemy.destroy();
        }
    }
}
