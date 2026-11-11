package com.towerdefense.scene;

interface GamePhase {

    void update(GameScene scene, double deltaTime);

    GamePhase togglePause();

    boolean canPlaceTower();

    boolean isPaused();

    boolean isGameOver();
}
