package com.towerdefense.scene;

final class PlayingState implements GamePhase {

    @Override
    public void update(GameScene scene, double deltaTime) {
        scene.updateGameplay(deltaTime);
    }

    @Override
    public GamePhase togglePause() {
        return new PausedState();
    }

    @Override
    public boolean canPlaceTower() {
        return true;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }
}
