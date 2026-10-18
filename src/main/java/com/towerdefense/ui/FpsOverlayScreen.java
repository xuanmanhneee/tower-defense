package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FpsOverlayScreen implements UIScreen {

    private static final double REFRESH_INTERVAL = 0.5;
    private static final double WIDTH = 92;
    private static final double HEIGHT = 32;
    private static final double MARGIN = 20;

    private double displayedFps;
    private double elapsed;
    private int frames;

    @Override
    public void update(double deltaTime) {
        if (deltaTime <= 0) {
            return;
        }

        elapsed += deltaTime;
        frames++;
        if (elapsed >= REFRESH_INTERVAL) {
            displayedFps = frames / elapsed;
            elapsed = 0;
            frames = 0;
        }
    }

    @Override
    public void render(GraphicsContext graphics) {
        double x = GameConfig.WINDOW_WIDTH - WIDTH - MARGIN;
        double y = MARGIN;

        graphics.setFill(Color.rgb(15, 18, 22, 0.82));
        graphics.fillRoundRect(x, y, WIDTH, HEIGHT, 9, 9);
        graphics.setFill(Color.WHITE);
        graphics.fillText(String.format("FPS  %.1f", displayedFps), x + 12, y + 21);
    }

    @Override
    public boolean handleClick(double x, double y) {
        return false;
    }
}
