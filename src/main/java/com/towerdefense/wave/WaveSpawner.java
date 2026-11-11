package com.towerdefense.wave;

import com.towerdefense.behaviour.PathFollower;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;
import com.towerdefense.core.Scene;
import com.towerdefense.core.Vector2;
import com.towerdefense.event.WavesClearedEvent;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class WaveSpawner {

    private static final int MAX_SPAWNS_PER_FRAME = 100;

    private final Scene scene;
    private final List<Vector2> waypoints;
    private final List<WaveData> waves;
    private final EnemyRegistry enemyRegistry;
    private final Set<GameObject> activeEnemies =
            Collections.newSetFromMap(new IdentityHashMap<>());

    private WaveState state = WaveState.SPAWNING;
    private int waveIndex;
    private int groupIndex;
    private int spawnedInGroup;
    private double timer;

    public WaveSpawner(
            Scene scene,
            List<Vector2> waypoints,
            List<WaveData> waves,
            EnemyRegistry enemyRegistry) {

        if (waypoints == null || waypoints.isEmpty()) {
            throw new IllegalArgumentException("Enemy path must contain at least one waypoint");
        }
        if (waves == null || waves.isEmpty()) {
            throw new IllegalArgumentException("At least one wave is required");
        }

        this.scene = scene;
        this.waypoints = List.copyOf(waypoints);
        this.waves = List.copyOf(waves);
        this.enemyRegistry = enemyRegistry;

        validateEnemyIds();
        scene.getEventBus().subscribe(EnemyRemovedEvent.class, this::onEnemyRemoved);
    }

    public void update(double deltaTime) {
        if (deltaTime < 0) {
            throw new IllegalArgumentException("deltaTime must not be negative");
        }

        switch (state) {
            case SPAWNING -> updateSpawning(deltaTime);
            case WAITING_FOR_CLEAR -> updateWaitingForClear();
            case BETWEEN_WAVES -> updateBetweenWaves(deltaTime);
            case COMPLETED -> {
            }
        }
    }

    private void updateSpawning(double deltaTime) {
        timer -= deltaTime;
        int spawnedThisFrame = 0;

        while (state == WaveState.SPAWNING
                && timer <= 0
                && spawnedThisFrame < MAX_SPAWNS_PER_FRAME) {

            SpawnGroupData group = currentWave().groups().get(groupIndex);
            spawn(group.enemy());
            spawnedInGroup++;
            spawnedThisFrame++;

            if (spawnedInGroup < group.count()) {
                timer += group.interval();
                continue;
            }

            groupIndex++;
            spawnedInGroup = 0;

            if (groupIndex >= currentWave().groups().size()) {
                state = WaveState.WAITING_FOR_CLEAR;
            }
        }
    }

    private void updateWaitingForClear() {
        if (!activeEnemies.isEmpty()) {
            return;
        }

        if (waveIndex >= waves.size() - 1) {
            state = WaveState.COMPLETED;
            scene.getEventBus().publish(new WavesClearedEvent());
            return;
        }

        timer = currentWave().delayAfter();
        state = WaveState.BETWEEN_WAVES;
    }

    private void updateBetweenWaves(double deltaTime) {
        timer -= deltaTime;
        if (timer > 0) {
            return;
        }

        waveIndex++;
        groupIndex = 0;
        spawnedInGroup = 0;
        state = WaveState.SPAWNING;
    }

    private void spawn(String enemyId) {
        Prefab prefab = enemyRegistry.get(enemyId);
        GameObject enemy = scene.instantiate(prefab, waypoints.get(0));
        enemy.getBehaviour(PathFollower.class).setWaypoints(waypoints);
        activeEnemies.add(enemy);
    }

    private void onEnemyRemoved(EnemyRemovedEvent event) {
        activeEnemies.remove(event.enemy());
    }

    private WaveData currentWave() {
        return waves.get(waveIndex);
    }

    private void validateEnemyIds() {
        for (int i = 0; i < waves.size(); i++) {
            for (SpawnGroupData group : waves.get(i).groups()) {
                if (!enemyRegistry.contains(group.enemy())) {
                    throw new IllegalArgumentException(
                            "Wave " + (i + 1) + " references unknown enemy: " + group.enemy());
                }
            }
        }
    }

    public WaveState getState() {
        return state;
    }

    public int getCurrentWaveNumber() {
        return waveIndex + 1;
    }

    public int getWaveCount() {
        return waves.size();
    }

    public int getActiveEnemyCount() {
        return activeEnemies.size();
    }

    public double getTimeUntilNextWave() {
        return state == WaveState.BETWEEN_WAVES ? Math.max(0, timer) : 0;
    }
}
