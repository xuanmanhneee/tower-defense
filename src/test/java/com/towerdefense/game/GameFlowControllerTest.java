package com.towerdefense.game;

import com.towerdefense.config.GameConfig;
import com.towerdefense.event.DeathEvent;
import com.towerdefense.event.GameOverEvent;
import com.towerdefense.scene.GameScene;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFlowControllerTest {

    @Test
    void startsAtMainMenuWithoutCreatingAGameScene() {
        GameFlowController controller = new GameFlowController();

        assertEquals(GameFlowController.View.MAIN_MENU, controller.getView());
        assertEquals(Optional.empty(), controller.getCurrentGameScene());

        controller.update(1.0);

        assertEquals(GameFlowController.View.MAIN_MENU, controller.getView());
        assertEquals(Optional.empty(), controller.getCurrentGameScene());
    }

    @Test
    void startsNewSessionWithDefaultResources() {
        GameFlowController controller = new GameFlowController();

        GameScene started = controller.startNewGame();

        assertEquals(GameFlowController.View.PLAYING, controller.getView());
        assertSame(started, controller.getCurrentGameScene().orElseThrow());
        assertEquals(GameConfig.STARTING_GOLD, GameState.getInstance().getGold());
        assertEquals(GameConfig.STARTING_LIVES, GameState.getInstance().getLives());
    }

    @Test
    void escapePausesAndResumesTheSameSession() {
        GameFlowController controller = new GameFlowController();
        GameScene session = controller.startNewGame();

        controller.handleEscape();
        assertEquals(GameFlowController.View.PAUSED, controller.getView());
        assertTrue(session.isPaused());

        controller.handleEscape();
        assertEquals(GameFlowController.View.PLAYING, controller.getView());
        assertFalse(session.isPaused());
        assertSame(session, controller.getCurrentGameScene().orElseThrow());
    }

    @Test
    void returningToMenuDiscardsTheCurrentScene() {
        GameFlowController controller = new GameFlowController();
        controller.startNewGame();

        controller.returnToMainMenu();

        assertEquals(GameFlowController.View.MAIN_MENU, controller.getView());
        assertEquals(Optional.empty(), controller.getCurrentGameScene());
        controller.update(1.0);
        assertEquals(Optional.empty(), controller.getCurrentGameScene());
    }

    @Test
    void startingAnotherSessionAfterMenuResetsSceneStatsAndResources() {
        GameFlowController controller = new GameFlowController();
        GameScene first = controller.startNewGame();
        first.getScene().getEventBus().publish(new DeathEvent(null, 12));
        controller.returnToMainMenu();

        GameScene next = controller.startNewGame();

        assertNotSame(first, next);
        assertNotSame(first.getMap(), next.getMap());
        assertNotSame(first.getScene(), next.getScene());
        assertEquals(0, next.getSessionStats().getEnemiesDefeated());
        assertEquals(0, next.getSessionStats().getGoldEarned());
        assertEquals(GameConfig.STARTING_GOLD, GameState.getInstance().getGold());
        assertEquals(GameConfig.STARTING_LIVES, GameState.getInstance().getLives());
    }

    @Test
    void startingNewSessionFromPauseRestartsTheGame() {
        GameFlowController controller = new GameFlowController();
        GameScene paused = controller.startNewGame();
        paused.getScene().getEventBus().publish(new DeathEvent(null, 12));
        controller.pause();

        GameScene restarted = controller.startNewGame();

        assertNotSame(paused, restarted);
        assertEquals(GameFlowController.View.PLAYING, controller.getView());
        assertEquals(0, restarted.getSessionStats().getEnemiesDefeated());
        assertEquals(0, restarted.getSessionStats().getGoldEarned());
        assertEquals(GameConfig.STARTING_GOLD, GameState.getInstance().getGold());
        assertEquals(GameConfig.STARTING_LIVES, GameState.getInstance().getLives());
    }

    @Test
    void startingNewSessionFromGameOverRestartsTheGame() {
        GameFlowController controller = new GameFlowController();
        GameScene over = controller.startNewGame();
        over.getScene().getEventBus().publish(new GameOverEvent());
        controller.update(0);

        GameScene restarted = controller.startNewGame();

        assertNotSame(over, restarted);
        assertEquals(GameFlowController.View.PLAYING, controller.getView());
        assertEquals(0, restarted.getSessionStats().getEnemiesDefeated());
        assertEquals(0, restarted.getSessionStats().getGoldEarned());
        assertEquals(GameConfig.STARTING_GOLD, GameState.getInstance().getGold());
        assertEquals(GameConfig.STARTING_LIVES, GameState.getInstance().getLives());
    }

    @Test
    void gameOverIsTerminalAndEscapeDoesNotResume() {
        GameFlowController controller = new GameFlowController();
        GameScene session = controller.startNewGame();
        session.getScene().getEventBus().publish(new GameOverEvent());

        controller.update(0);
        controller.handleEscape();

        assertEquals(GameFlowController.View.GAME_OVER, controller.getView());
        assertTrue(session.isGameOver());
        assertFalse(session.isPaused());
    }

    @Test
    void gameOverPublishedWhilePausedTakesPrecedenceOverPause() {
        GameFlowController controller = new GameFlowController();
        GameScene session = controller.startNewGame();
        controller.pause();
        session.getScene().getEventBus().publish(new GameOverEvent());

        controller.update(0);

        assertEquals(GameFlowController.View.GAME_OVER, controller.getView());
        assertTrue(session.isGameOver());
        assertFalse(session.isPaused());
    }

    @Test
    void pausingAnAlreadyGameOverSceneDoesNotSetPausedView() {
        GameFlowController controller = new GameFlowController();
        GameScene session = controller.startNewGame();
        session.getScene().getEventBus().publish(new GameOverEvent());

        controller.pause();

        assertEquals(GameFlowController.View.GAME_OVER, controller.getView());
        assertTrue(session.isGameOver());
        assertFalse(session.isPaused());
    }

    @Test
    void invalidPauseAndResumeCallsDoNotChangeViewOrScene() {
        GameFlowController controller = new GameFlowController();
        controller.pause();
        controller.resume();
        assertEquals(GameFlowController.View.MAIN_MENU, controller.getView());
        assertEquals(Optional.empty(), controller.getCurrentGameScene());

        GameScene session = controller.startNewGame();
        controller.resume();
        assertEquals(GameFlowController.View.PLAYING, controller.getView());
        assertSame(session, controller.getCurrentGameScene().orElseThrow());

        controller.pause();
        controller.pause();
        assertEquals(GameFlowController.View.PAUSED, controller.getView());
        assertSame(session, controller.getCurrentGameScene().orElseThrow());
    }
}
