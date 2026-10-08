package com.towerdefense.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameHudScreen implements UIScreen {

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {

        // HUD background
        graphics.setFill(Color.rgb(0, 0, 0, 0.7));
        graphics.fillRect(20, 20, 200, 80);

        // HUD text
        graphics.setFill(Color.WHITE);
        graphics.fillText("GAME HUD", 40, 50);
        graphics.fillText("Gold: 100", 40, 75);
    }

    @Override
    public void handleClick(double x, double y) {
    }
}