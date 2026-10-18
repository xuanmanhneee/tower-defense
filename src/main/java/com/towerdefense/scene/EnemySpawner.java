package com.towerdefense.scene;

import com.towerdefense.behaviour.PathFollower;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;
import com.towerdefense.core.Scene;
import com.towerdefense.core.Vector2;

import java.util.List;

public class EnemySpawner {

    private final Scene scene;
    private final List<Prefab> enemyPrefabs;
    private final List<Vector2> waypoints;
    private final double spawnInterval;

    private double spawnTimer = 0;
    private int spawnCount = 0;

    public EnemySpawner(Scene scene, Prefab enemyPrefab, List<Vector2> waypoints, double spawnInterval) {
        this(scene, List.of(enemyPrefab), waypoints, spawnInterval);
    }

    public EnemySpawner(Scene scene, List<Prefab> enemyPrefabs, List<Vector2> waypoints, double spawnInterval) {
        this.scene = scene;
        this.enemyPrefabs = List.copyOf(enemyPrefabs);
        this.waypoints = waypoints;
        this.spawnInterval = spawnInterval;
    }

    public void update(double deltaTime) {
        spawnTimer -= deltaTime;

        if (spawnTimer <= 0) {
            spawn();
            spawnTimer = spawnInterval;
        }
    }

    private void spawn() {
        if (enemyPrefabs.isEmpty()) return;

        spawnCount++;
        Prefab chosenPrefab;

        if (enemyPrefabs.size() == 1) {
            chosenPrefab = enemyPrefabs.get(0);
        } else {
            // Predictable wave variety:
            // Every 6th enemy is a Heavy Tank
            // Every 3rd enemy is a Fast Scout
            // Otherwise standard Grunt
            if (spawnCount % 6 == 0 && enemyPrefabs.size() >= 3) {
                chosenPrefab = enemyPrefabs.get(2); // Tank
            } else if (spawnCount % 3 == 0 && enemyPrefabs.size() >= 2) {
                chosenPrefab = enemyPrefabs.get(1); // Scout
            } else {
                chosenPrefab = enemyPrefabs.get(0); // Grunt
            }
        }

        GameObject enemy = scene.instantiate(chosenPrefab, waypoints.get(0));
        enemy.getBehaviour(PathFollower.class).setWaypoints(waypoints);
    }
}