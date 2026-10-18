package com.towerdefense.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.canvas.GraphicsContext;

public class UIManager {

    private final List<UIScreen> screens = new ArrayList<>();

    public void setScreen(UIScreen screen) {
        screens.clear();
        screens.add(screen);
    }

    public void addScreen(UIScreen screen) {
        screens.add(screen);
    }

    public void update(double deltaTime) {
        for (UIScreen screen : screens) {
            screen.update(deltaTime);
        }
    }

    public void render(GraphicsContext graphics) {
        for (UIScreen screen : screens) {
            screen.render(graphics);
        }
    }

    public boolean handleClick(double x, double y) {
        for (int i = screens.size() - 1; i >= 0; i--) {
            if (screens.get(i).handleClick(x, y)) {
                return true;
            }
        }
        return false;
    }

    public void showTowerSelection(
            List<TowerOption> towers,
            Consumer<TowerOption> onSelected) {
        addScreen(
                new TowerSelectionScreen(
                        towers,
                        onSelected));
    }
}
