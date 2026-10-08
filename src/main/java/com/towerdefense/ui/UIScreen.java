package com.towerdefense.ui;

import javafx.scene.canvas.GraphicsContext;

public interface UIScreen {

    void update(double deltaTime);

    void render(GraphicsContext graphics);

    void handleClick(double x, double y);
}