package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import java.util.function.Supplier;

import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.game.GameState;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameHudScreen implements UIScreen {

    private final GameState gameState;
    private final Supplier<TowerType> selectedTower;

    public GameHudScreen(GameState gameState, Supplier<TowerType> selectedTower) {
        this.gameState = gameState;
        this.selectedTower = selectedTower;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {

        // HUD background
        graphics.setFill(Color.rgb(0, 0, 0, 0.7));
        graphics.fillRect(20, 20, 240, 125);

        // HUD text
        graphics.setFill(Color.WHITE);
        graphics.fillText("TOWER DEFENSE", 40, 45);
        graphics.fillText("Gold: " + gameState.getGold(), 40, 70);

        TowerType tower = selectedTower.get();
        graphics.fillText(
                "Selected: " + tower.getDisplayName() + " ($" + tower.getCost() + ")",
                40,
                95);

        graphics.fillText("Base HP: " + gameState.getLives(), 40, 120);

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
