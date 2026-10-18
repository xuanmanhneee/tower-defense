package com.towerdefense.scene;

import com.towerdefense.behaviour.Health;
import com.towerdefense.behaviour.PathFollower;
import com.towerdefense.collision.CircleCollider;
import com.towerdefense.collision.CollisionLayer;
import com.towerdefense.collision.CollisionListener;
import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;
import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.event.GameOverEvent;
import com.towerdefense.game.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSceneStateTest {

    @Test
    void pauseAndResumeControlTheCurrentPhase() {
        GameScene game = new GameScene();

        assertFalse(game.isPaused());
        assertFalse(game.isGameOver());
        game.togglePause();
        assertTrue(game.isPaused());
        game.togglePause();
        assertFalse(game.isPaused());
    }

    @Test
    void pausedSimulationFreezesEnemyAndSpawnerUntilResumed() {
        GameScene game = new GameScene();
        game.update(0.0);
        game.update(0.0);
        GameObject enemy = game.getScene()
                .findObjectsWithBehaviour(PathFollower.class).get(0);
        Vector2 positionBeforePause = enemy.getTransform().getPosition();
        int objectCountBeforePause = game.getScene().getGameObjects().size();

        game.togglePause();
        game.update(2.0);
        assertEquals(positionBeforePause, enemy.getTransform().getPosition());
        assertEquals(objectCountBeforePause, game.getScene().getGameObjects().size());

        game.togglePause();
        game.update(0.5);
        assertNotEquals(positionBeforePause, enemy.getTransform().getPosition());
        assertEquals(objectCountBeforePause, game.getScene().getGameObjects().size());
    }

    @Test
    void pausedSimulationSkipsCollisionsUntilResumed() {
        GameScene game = new GameScene();
        game.update(0.0);
        GameObject goal = game.getScene()
                .findObjectsWithBehaviour(Health.class).get(0);
        GameObject probe = new GameObject();
        probe.addBehaviour(new CircleCollider(8, CollisionLayer.PROJECTILE)
                .detects(CollisionLayer.GOAL));
        CollisionProbe listener = probe.addBehaviour(new CollisionProbe());
        game.getScene().instantiate(
                () -> probe,
                goal.getTransform().getPosition());
        game.update(0.0);

        game.togglePause();
        game.update(0.1);
        assertEquals(0, listener.collisions);

        game.togglePause();
        game.update(0.1);
        assertEquals(1, listener.collisions);
    }

    @Test
    void pausedBuildAndUpgradeAreRejectedWithoutSpendingOrOccupying() {
        GameScene game = new GameScene();
        GameState state = GameState.getInstance();
        assertTrue(game.tryPlaceTower(96, 192, TowerType.BASIC));
        int goldBeforePausedAttempts = state.getGold();

        game.togglePause();
        assertFalse(game.tryPlaceTower(224, 192, TowerType.BASIC));
        assertFalse(game.tryPlaceTower(96, 192, TowerType.SPREAD));
        assertEquals(goldBeforePausedAttempts, state.getGold());

        game.togglePause();
        assertTrue(game.tryPlaceTower(224, 192, TowerType.BASIC));
        assertEquals(goldBeforePausedAttempts - TowerType.BASIC.getCost(), state.getGold());
    }

    @Test
    void goalDeathEntersTerminalGameOverState() {
        GameScene game = new GameScene();
        game.update(0.0);
        GameObject goal = game.getScene()
                .findObjectsWithBehaviour(Health.class).get(0);

        goal.getBehaviour(Health.class).damage(20);
        game.togglePause();

        assertTrue(game.isGameOver());
        assertFalse(game.isPaused());
        assertEquals(0, GameState.getInstance().getLives());
        assertFalse(game.tryPlaceTower(96, 192, TowerType.BASIC));
    }

    @Test
    void gameOverEventWhilePausedCannotBeResumed() {
        GameScene game = new GameScene();
        game.togglePause();

        game.getScene().getEventBus().publish(new GameOverEvent());
        game.togglePause();

        assertTrue(game.isGameOver());
        assertFalse(game.isPaused());
        assertFalse(game.tryPlaceTower(96, 192, TowerType.BASIC));
    }

    private static final class CollisionProbe extends Behaviour
            implements CollisionListener {
        private int collisions;

        @Override
        public void onCollision(GameObject other) {
            collisions++;
        }
    }
}
