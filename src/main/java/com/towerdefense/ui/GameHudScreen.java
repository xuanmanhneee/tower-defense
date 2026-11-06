package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import java.util.function.Supplier;

import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.game.GameState;
import com.towerdefense.wave.WaveSpawner;
import com.towerdefense.wave.WaveState;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameHudScreen implements UIScreen {

    private final GameState gameState;
    private final Supplier<TowerType> selectedTower;
    private final WaveSpawner waveSpawner;

    public GameHudScreen(
            GameState gameState,
            Supplier<TowerType> selectedTower,
            WaveSpawner waveSpawner) {
        this.gameState = gameState;
        this.selectedTower = selectedTower;
        this.waveSpawner = waveSpawner;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {

        // HUD background
        graphics.setFill(Color.rgb(0, 0, 0, 0.7));
        graphics.fillRect(20, 20, 240, 170);

        // HUD text
        graphics.setFill(Color.WHITE);
        graphics.fillText("TOWER DEFENSE", 40, 45);
        graphics.fillText("Gold: " + gameState.getGold(), 40, 70);

        TowerType tower = selectedTower.get();
        String action = tower.requiresExistingTower() ? "Upgrade" : "Build";
        graphics.fillText(
                action + ": " + tower.getDisplayName() + " ($" + tower.getCost() + ")",
                40,
                95);

        graphics.fillText("Base HP: " + gameState.getLives(), 40, 120);

        String waveText = waveSpawner.getState() == WaveState.COMPLETED
                ? "All waves cleared"
                : "Wave: " + waveSpawner.getCurrentWaveNumber()
                        + " / " + waveSpawner.getWaveCount();
        graphics.fillText(waveText, 40, 145);

        String enemyText = waveSpawner.getState() == WaveState.BETWEEN_WAVES
                ? String.format("Next wave: %.1fs", waveSpawner.getTimeUntilNextWave())
                : "Enemies: " + waveSpawner.getActiveEnemyCount();
        graphics.fillText(enemyText, 40, 170);

        if (gameState.isGameOver()) {
            graphics.setFill(Color.rgb(0, 0, 0, 0.72));
            graphics.fillRect(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

            graphics.setFill(Color.WHITE);
            graphics.fillText(
                    "GAME OVER",
                    GameConfig.WINDOW_WIDTH / 2.0 - 45,
                    GameConfig.WINDOW_HEIGHT / 2.0);
        }
    }

    @Override
    public boolean handleClick(double x, double y) {
        return gameState.isGameOver();
    }
}
