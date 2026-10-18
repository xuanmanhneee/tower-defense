package com.towerdefense.game;

import com.towerdefense.scene.GameScene;

import java.util.Optional;

public final class GameFlowController {

    public enum View {
        MAIN_MENU,
        PLAYING,
        PAUSED,
        GAME_OVER
    }

    private View view = View.MAIN_MENU;
    private GameScene currentGameScene;

    public View getView() {
        return view;
    }

    public Optional<GameScene> getCurrentGameScene() {
        return Optional.ofNullable(currentGameScene);
    }

    public GameScene startNewGame() {
        currentGameScene = new GameScene();
        view = View.PLAYING;
        return currentGameScene;
    }

    public void pause() {
        if (view != View.PLAYING) {
            return;
        }

        if (currentGameScene.isGameOver()) {
            view = View.GAME_OVER;
            return;
        }

        currentGameScene.togglePause();
        updateViewFromScene();
    }

    public void resume() {
        if (view != View.PAUSED) {
            return;
        }

        if (currentGameScene.isGameOver()) {
            view = View.GAME_OVER;
            return;
        }

        currentGameScene.togglePause();
        updateViewFromScene();
    }

    public void handleEscape() {
        switch (view) {
            case PLAYING -> pause();
            case PAUSED -> resume();
            case MAIN_MENU, GAME_OVER -> {
            }
        }
    }

    public void returnToMainMenu() {
        currentGameScene = null;
        view = View.MAIN_MENU;
    }

    public void update(double deltaTime) {
        if (currentGameScene == null) {
            return;
        }

        currentGameScene.update(deltaTime);
        if (currentGameScene.isGameOver()) {
            view = View.GAME_OVER;
        }
    }

    private void updateViewFromScene() {
        if (currentGameScene.isGameOver()) {
            view = View.GAME_OVER;
        } else if (currentGameScene.isPaused()) {
            view = View.PAUSED;
        } else {
            view = View.PLAYING;
        }
    }
}
