package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import java.util.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MainMenuScreen implements UIScreen {

    private static final double PANEL_WIDTH = 420;
    private static final double PANEL_HEIGHT = 300;
    private static final double BUTTON_X = (GameConfig.WINDOW_WIDTH - MenuButton.WIDTH) / 2.0;

    private final MenuButton[] buttons;

    public MainMenuScreen(Runnable play, Runnable quit) {
        buttons = new MenuButton[]{
                new MenuButton(
                        BUTTON_X,
                        buttonTopY(0),
                        MenuButton.WIDTH,
                        MenuButton.HEIGHT,
                        "Play",
                        MenuButton.Variant.PRIMARY,
                        Objects.requireNonNull(play)),
                new MenuButton(
                        BUTTON_X,
                        buttonTopY(1),
                        MenuButton.WIDTH,
                        MenuButton.HEIGHT,
                        "Quit",
                        MenuButton.Variant.SECONDARY,
                        Objects.requireNonNull(quit))
        };
    }

    static double buttonTopY(int index) {
        if (index < 0 || index >= 2) {
            throw new IllegalArgumentException("Main menu button index is out of range");
        }
        return (GameConfig.WINDOW_HEIGHT - PANEL_HEIGHT) / 2.0 + 155 + index * 64;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {
        graphics.save();
        graphics.setFill(Color.rgb(12, 17, 25));
        graphics.fillRect(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        double panelX = (GameConfig.WINDOW_WIDTH - PANEL_WIDTH) / 2.0;
        double panelY = (GameConfig.WINDOW_HEIGHT - PANEL_HEIGHT) / 2.0;
        graphics.setFill(Color.rgb(30, 30, 30, 0.97));
        graphics.fillRoundRect(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 18, 18);

        graphics.setFill(Color.WHITE);
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setFont(Font.font(32));
        graphics.fillText(
                "TOWER DEFENSE",
                GameConfig.WINDOW_WIDTH / 2.0,
                panelY + 92);
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
        return false;
    }
}
