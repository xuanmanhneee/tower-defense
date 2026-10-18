package com.towerdefense.scene;

import com.towerdefense.collision.CollisionSystem;
import com.towerdefense.config.GameConfig;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Scene;
import com.towerdefense.core.Vector2;
import com.towerdefense.behaviour.Health;
import com.towerdefense.event.DeathEvent;
import com.towerdefense.event.GameOverEvent;
import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.game.GameState;
import com.towerdefense.map.GameMap;
import com.towerdefense.map.MapFactory;
import com.towerdefense.prefab.GoalPrefab;
import com.towerdefense.prefab.enemy.GruntEnemyPrefab;
import com.towerdefense.prefab.tower.TowerFactory;

public class GameScene {

    private final Scene scene = new Scene();
    private final GameMap map;
    private final CollisionSystem collisionSystem = new CollisionSystem();
    private final EnemySpawner enemySpawner;

    private GameObject goal;
    private boolean gameOver;

    public GameScene() {
        GameState.getInstance().reset();
        map = MapFactory.createLevel1Map();

        enemySpawner = new EnemySpawner(scene, new GruntEnemyPrefab(), map.getWaypoints(), 1.6);

        scene.getEventBus().subscribe(DeathEvent.class,
                event -> GameState.getInstance().addGold(event.reward()));
        scene.getEventBus().subscribe(GameOverEvent.class,
                event -> gameOver = true);

        spawnGoal();
    }

    private void spawnGoal() {
        Vector2 end = map.getWaypoints().get(map.getWaypoints().size() - 1);
        goal = scene.instantiate(new GoalPrefab(), end);
        Health goalHealth = goal.getBehaviour(Health.class);
        goalHealth.setOnDamaged(
                damage -> GameState.getInstance().loseLife((int) Math.ceil(damage)));
        goalHealth.setOnDeath(
                () -> scene.getEventBus().publish(new GameOverEvent()));
    }

    public void update(double deltaTime) {
        if (gameOver) {
            return;
        }

        collisionSystem.update(scene.getGameObjects());

        if (gameOver) {
            return;
        }

        scene.update(deltaTime);
        enemySpawner.update(deltaTime);
    }

    public boolean tryPlaceTower(double x, double y, TowerType towerType) {
        if (gameOver) {
            return false;
        }

        int row = map.rowFromY(y);
        int col = map.colFromX(x);

        int[] block = map.findBlockContaining(row, col);
        if (block == null || !map.isBlockFree(block)) {
            return false;
        }

        GameState gameState = GameState.getInstance();
        if (!gameState.spendGold(towerType.getCost())) {
            return false;
        }

        int size = GameConfig.TOWER_SIZE_IN_TILES;
        double centerX = block[1] * GameMap.TILE_SIZE + (size * GameMap.TILE_SIZE) / 2.0;
        double centerY = block[0] * GameMap.TILE_SIZE + (size * GameMap.TILE_SIZE) / 2.0;

        scene.instantiate(TowerFactory.create(towerType), new Vector2(centerX, centerY));
        map.markBlockOccupied(block);
        return true;
    }

    public Scene getScene() {
        return scene;
    }

    public GameMap getMap() {
        return map;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
