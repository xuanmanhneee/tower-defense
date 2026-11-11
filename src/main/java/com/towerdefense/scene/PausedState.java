package com.towerdefense.scene;

final class PausedState implements GamePhase {

    @Override
    public void update(GameScene scene, double deltaTime) {
    }

    @Override
    public GamePhase togglePause() {
        return new PlayingState();
    }

    @Override
    public boolean canPlaceTower() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return true;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }
}
