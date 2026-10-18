package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import java.util.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PauseMenuScreen implements UIScreen {

    private static final double PANEL_WIDTH = 440;
    private static final double PANEL_HEIGHT = 380;
    private static final double BUTTON_X = (GameConfig.WINDOW_WIDTH - MenuButton.WIDTH) / 2.0;

    private final MenuButton[] buttons;

    public PauseMenuScreen(Runnable resume, Runnable restart, Runnable mainMenu) {
        buttons = new MenuButton[]{
                createButton(0, "Resume", MenuButton.Variant.PRIMARY, resume),
                createButton(1, "Restart", MenuButton.Variant.SECONDARY, restart),
                createButton(2, "Main Menu", MenuButton.Variant.SECONDARY, mainMenu)
        };
    }

    static double buttonTopY(int index) {
        if (index < 0 || index >= 3) {
            throw new IllegalArgumentException("Pause menu button index is out of range");
        }
        return (GameConfig.WINDOW_HEIGHT - PANEL_HEIGHT) / 2.0 + 155 + index * 64;
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

        graphics.setFill(Color.WHITE);
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setFont(Font.font(32));
        graphics.fillText("PAUSED", GameConfig.WINDOW_WIDTH / 2.0, panelY + 90);
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
