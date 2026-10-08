package com.towerdefense.scene;

import com.towerdefense.behaviour.PathFollower;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;
import com.towerdefense.core.Scene;
import com.towerdefense.core.Vector2;

import java.util.List;

public class EnemySpawner {

    private final Scene scene;
    private final Prefab enemyPrefab;
    private final List<Vector2> waypoints;
    private final double spawnInterval;

    private double spawnTimer = 0;

    public EnemySpawner(Scene scene, Prefab enemyPrefab, List<Vector2> waypoints, double spawnInterval) {
        this.scene = scene;
        this.enemyPrefab = enemyPrefab;
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
        GameObject enemy = scene.instantiate(enemyPrefab, waypoints.get(0));
        enemy.getBehaviour(PathFollower.class).setWaypoints(waypoints);
    }
}