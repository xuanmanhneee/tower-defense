package com.towerdefense.scene;

import com.towerdefense.collision.CollisionSystem;
import com.towerdefense.config.GameConfig;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.PooledPrefab;
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

import java.util.HashMap;
import java.util.Map;

public class GameScene {

    private final Scene scene = new Scene();
    private final GameMap map;
    private final CollisionSystem collisionSystem = new CollisionSystem();
    private final EnemySpawner enemySpawner;
    private final Map<BlockPosition, PlacedTower> placedTowers = new HashMap<>();

    private GameObject goal;
    private GamePhase phase = new PlayingState();

    public GameScene() {
        GameState.getInstance().reset();
        map = MapFactory.createLevel1Map();

        enemySpawner = new EnemySpawner(scene, new PooledPrefab(new GruntEnemyPrefab(), 32), map.getWaypoints(), 1.6);

        scene.getEventBus().subscribe(DeathEvent.class,
                event -> GameState.getInstance().addGold(event.reward()));
        scene.getEventBus().subscribe(GameOverEvent.class,
                event -> phase = new GameOverState());

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
        phase.update(this, deltaTime);
    }

    void updateGameplay(double deltaTime) {
        collisionSystem.update(scene.getGameObjects());

        if (phase.isGameOver()) {
            return;
        }

        scene.update(deltaTime);
        enemySpawner.update(deltaTime);
    }

    public boolean tryPlaceTower(double x, double y, TowerType towerType) {
        if (!phase.canPlaceTower() || towerType == null) {
            return false;
        }

        int row = map.rowFromY(y);
        int col = map.colFromX(x);

        int[] block = map.findBlockContaining(row, col);
        if (block == null) {
            return false;
        }

        BlockPosition blockPosition = new BlockPosition(block[0], block[1]);
        PlacedTower existingTower = placedTowers.get(blockPosition);

        if (towerType.requiresExistingTower()) {
            return tryUpgradeTower(block, blockPosition, existingTower, towerType);
        }

        if (existingTower != null || !map.isBlockFree(block)) {
            return false;
        }

        GameState gameState = GameState.getInstance();
        if (!gameState.spendGold(towerType.getCost())) {
            return false;
        }

        GameObject tower = scene.instantiate(
                TowerFactory.create(towerType),
                centerOf(block));
        placedTowers.put(blockPosition, new PlacedTower(towerType, tower));
        map.markBlockOccupied(block);
        return true;
    }

    private boolean tryUpgradeTower(
            int[] block,
            BlockPosition blockPosition,
            PlacedTower existingTower,
            TowerType upgradeType) {

        if (existingTower == null || !upgradeType.canBuildOn(existingTower.type())) {
            return false;
        }

        GameState gameState = GameState.getInstance();
        if (!gameState.spendGold(upgradeType.getCost())) {
            return false;
        }

        existingTower.gameObject().destroy();
        GameObject upgradedTower = scene.instantiate(
                TowerFactory.create(upgradeType),
                centerOf(block));
        placedTowers.put(blockPosition, new PlacedTower(upgradeType, upgradedTower));
        return true;
    }

    private Vector2 centerOf(int[] block) {
        int size = GameConfig.TOWER_SIZE_IN_TILES;
        double centerX = block[1] * GameMap.TILE_SIZE + (size * GameMap.TILE_SIZE) / 2.0;
        double centerY = block[0] * GameMap.TILE_SIZE + (size * GameMap.TILE_SIZE) / 2.0;
        return new Vector2(centerX, centerY);
    }

    private record BlockPosition(int row, int col) {
    }

    private record PlacedTower(TowerType type, GameObject gameObject) {
    }

    public Scene getScene() {
        return scene;
    }

    public GameMap getMap() {
        return map;
    }

    public boolean isGameOver() {
        return phase.isGameOver();
    }

    public void togglePause() {
        phase = phase.togglePause();
    }

    public boolean isPaused() {
        return phase.isPaused();
    }
}
