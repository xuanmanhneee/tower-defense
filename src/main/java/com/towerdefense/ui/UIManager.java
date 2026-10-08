package com.towerdefense.ui;

import java.util.List;
import java.util.function.Consumer;

import javafx.scene.canvas.GraphicsContext;

public class UIManager {

    private UIScreen currentScreen;

    public void setScreen(UIScreen screen) {
        currentScreen = screen;
    }

    public void update(double deltaTime) {
        if (currentScreen != null) {
            currentScreen.update(deltaTime);
        }
    }

    public void render(GraphicsContext graphics) {
        if (currentScreen != null) {
            currentScreen.render(graphics);
        }
    }

    public void handleClick(double x, double y) {
        if (currentScreen != null) {
            currentScreen.handleClick(x, y);
        }
    }

    public void showTowerSelection(
            List<TowerOption> towers,
            Consumer<TowerOption> onSelected) {
        setScreen(
                new TowerSelectionScreen(
                        towers,
                        onSelected));
    }
}