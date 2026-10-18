package com.towerdefense.ui;

import java.util.function.Supplier;

import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.game.GameState;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameHudScreen implements UIScreen {

    private final GameState gameState;
    private final Supplier<TowerType> selectedTower;

    public GameHudScreen(
            GameState gameState,
            Supplier<TowerType> selectedTower) {
        this.gameState = gameState;
        this.selectedTower = selectedTower;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {

        double panelX = 20;
        double panelY = 20;
        double panelWidth = 260;
        double panelHeight = 125;

        // Panel background - matching Choose Tower panel style
        graphics.setFill(Color.rgb(30, 30, 30, 0.95));
        graphics.fillRoundRect(
                panelX,
                panelY,
                panelWidth,
                panelHeight,
                15,
                15
        );

        // Header Title
        graphics.setFill(Color.WHITE);
        graphics.fillText(
                "Tower Defense",
                panelX + 20,
                panelY + 26
        );

        // Row 1: Two cards (Gold & Base HP) side by side
        double row1Y = panelY + 38;
        double cardW = (panelWidth - 35) / 2.0;

        // Gold Card
        graphics.setFill(Color.rgb(60, 60, 60));
        graphics.fillRoundRect(
                panelX + 15,
                row1Y,
                cardW,
                32,
                8,
                8
        );
        graphics.setFill(Color.WHITE);
        graphics.fillText("Gold: " + gameState.getGold(), panelX + 23, row1Y + 20);

        // Base HP Card
        graphics.setFill(Color.rgb(60, 60, 60));
        graphics.fillRoundRect(
                panelX + 20 + cardW,
                row1Y,
                cardW,
                32,
                8,
                8
        );
        graphics.setFill(Color.WHITE);
        graphics.fillText("HP: " + gameState.getLives(), panelX + 28 + cardW, row1Y + 20);

        // Row 2: Selected Tower Action Card (matching active blue highlight)
        double row2Y = panelY + 76;
        TowerType tower = selectedTower.get();
        String action = tower.requiresExistingTower() ? "Upgrade" : "Build";

        graphics.setFill(Color.rgb(40, 115, 180));
        graphics.fillRoundRect(
                panelX + 15,
                row2Y,
                panelWidth - 30,
                34,
                8,
                8
        );

        graphics.setFill(Color.WHITE);
        graphics.fillText(
                action + ": " + tower.getDisplayName(),
                panelX + 25,
                row2Y + 21
        );

        graphics.fillText(
                "$" + tower.getCost(),
                panelX + panelWidth - 55,
                row2Y + 21
        );

    }

    @Override
    public boolean handleClick(double x, double y) {
        return false;
    }
}
