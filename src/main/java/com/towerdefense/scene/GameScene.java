package com.towerdefense.scene;

import com.towerdefense.collision.CollisionSystem;
import com.towerdefense.config.GameConfig;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Scene;
import com.towerdefense.core.Vector2;
import com.towerdefense.behaviour.Health;
import com.towerdefense.event.DeathEvent;
import com.towerdefense.event.GameOverEvent;
import com.towerdefense.game.GameState;
import com.towerdefense.map.GameMap;
import com.towerdefense.map.MapFactory;
import com.towerdefense.prefab.GoalPrefab;
import com.towerdefense.prefab.enemy.GruntEnemyPrefab;
import com.towerdefense.prefab.tower.ExplosiveTowerPrefab;

public class GameScene {

    private final Scene scene = new Scene();
    private final GameMap map;
    private final CollisionSystem collisionSystem = new CollisionSystem();
    private final EnemySpawner enemySpawner;

    private GameObject goal;

    public GameScene() {
        map = MapFactory.createLevel1Map();

        enemySpawner = new EnemySpawner(scene, new GruntEnemyPrefab(), map.getWaypoints(), 1.5);

        scene.getEventBus().subscribe(DeathEvent.class,
                event -> GameState.getInstance().addGold(event.reward()));

        spawnGoal();
    }

    private void spawnGoal() {
        Vector2 end = map.getWaypoints().get(map.getWaypoints().size() - 1);
        goal = scene.instantiate(new GoalPrefab(), end);
        goal.getBehaviour(Health.class).setOnDeath(
                () -> scene.getEventBus().publish(new GameOverEvent()));
    }

    public void update(double deltaTime) {
        collisionSystem.update(scene.getGameObjects());
        scene.update(deltaTime);
        enemySpawner.update(deltaTime);
    }

    public void handleClick(double x, double y) {
        int row = map.rowFromY(y);
        int col = map.colFromX(x);

        int[] block = map.findBlockContaining(row, col);
        if (block == null || !map.isBlockFree(block)) {
            return;
        }

        int size = GameConfig.TOWER_SIZE_IN_TILES;
        double centerX = block[1] * GameMap.TILE_SIZE + (size * GameMap.TILE_SIZE) / 2.0;
        double centerY = block[0] * GameMap.TILE_SIZE + (size * GameMap.TILE_SIZE) / 2.0;

        scene.instantiate(new ExplosiveTowerPrefab(), new Vector2(centerX, centerY));
        map.markBlockOccupied(block);
    }

    public Scene getScene() {
        return scene;
    }

    public GameMap getMap() {
        return map;
    }
}