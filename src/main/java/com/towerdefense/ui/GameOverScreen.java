package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import com.towerdefense.game.GameSessionStats;
import java.util.Objects;
import java.util.function.Supplier;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameOverScreen implements UIScreen {

    private static final double PANEL_WIDTH = 500;
    private static final double PANEL_HEIGHT = 400;
    private static final double BUTTON_X = (GameConfig.WINDOW_WIDTH - MenuButton.WIDTH) / 2.0;

    private final Supplier<GameSessionStats> stats;
    private final MenuButton[] buttons;

    public GameOverScreen(
            Supplier<GameSessionStats> stats,
            Runnable restart,
            Runnable mainMenu) {
        this.stats = Objects.requireNonNull(stats);
        buttons = new MenuButton[]{
                createButton(0, "Restart", MenuButton.Variant.PRIMARY, restart),
                createButton(1, "Main Menu", MenuButton.Variant.SECONDARY, mainMenu)
        };
    }

    static double buttonTopY(int index) {
        if (index < 0 || index >= 2) {
            throw new IllegalArgumentException("Game over button index is out of range");
        }
        return (GameConfig.WINDOW_HEIGHT - PANEL_HEIGHT) / 2.0 + 245 + index * 64;
    }

    private static MenuButton createButton(
            int index,
            String label,
            MenuButton.Variant variant,
            Runnable action) {
        return new MenuButton(
                BUTTON_X,
                buttonTopY(index),
                MenuButton.WIDTH,
                MenuButton.HEIGHT,
                label,
                variant,
                Objects.requireNonNull(action));
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {
        graphics.save();
        graphics.setFill(Color.rgb(0, 0, 0, 0.72));
        graphics.fillRect(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        double panelX = (GameConfig.WINDOW_WIDTH - PANEL_WIDTH) / 2.0;
        double panelY = (GameConfig.WINDOW_HEIGHT - PANEL_HEIGHT) / 2.0;
        graphics.setFill(Color.rgb(30, 30, 30, 0.98));
        graphics.fillRoundRect(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 18, 18);

        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setFill(Color.rgb(237, 119, 80));
        graphics.setFont(Font.font(32));
        graphics.fillText("GAME OVER", GameConfig.WINDOW_WIDTH / 2.0, panelY + 82);

        GameSessionStats result = Objects.requireNonNull(stats.get());
        graphics.setFill(Color.WHITE);
        graphics.setFont(Font.font(18));
        graphics.fillText(
                "Enemies defeated: " + result.getEnemiesDefeated(),
                GameConfig.WINDOW_WIDTH / 2.0,
                panelY + 140);
        graphics.fillText(
                "Gold earned: $" + result.getGoldEarned(),
                GameConfig.WINDOW_WIDTH / 2.0,
                panelY + 178);

        for (MenuButton button : buttons) {
            button.render(graphics);
        }
        graphics.restore();
    }

    @Override
    public boolean handleClick(double x, double y) {
        for (MenuButton button : buttons) {
            if (button.handleClick(x, y)) {
                return true;
            }
        }
        return true;
    }
}
