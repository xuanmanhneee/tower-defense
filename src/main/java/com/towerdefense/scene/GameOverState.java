package com.towerdefense.scene;

final class GameOverState implements GamePhase {

    @Override
    public void update(GameScene scene, double deltaTime) {
    }

    @Override
    public GamePhase togglePause() {
        return this;
    }

    @Override
    public boolean canPlaceTower() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public boolean isGameOver() {
        return true;
    }
}
