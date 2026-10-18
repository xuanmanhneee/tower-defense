package com.towerdefense.render;

import com.towerdefense.behaviour.Health;
import com.towerdefense.core.Vector2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthBarRenderer extends RenderBehaviour {

    private final double width;
    private final double height;
    private final double offsetY;

    public HealthBarRenderer(double width, double height, double offsetY) {
        super(RenderLayer.OVERLAY);
        this.width = width;
        this.height = height;
        this.offsetY = offsetY;
    }

    @Override
    public void render(GraphicsContext gc) {
        Health health = gameObject.getBehaviour(Health.class);
        if (health == null || health.isDead()) {
            return;
        }

        Vector2 position = gameObject.getTransform().getPosition();
        double x = position.x() - width / 2;
        double y = position.y() - offsetY;
        double ratio = Math.max(0, Math.min(1.0, health.getRatio()));

        // Outer Dark Capsule / Shield Plate
        gc.setFill(Color.web("#090d16", 0.85));
        gc.fillRoundRect(x - 1, y - 1, width + 2, height + 2, 4, 4);

        // Background Track (Lost HP)
        gc.setFill(Color.web("#334155", 0.6));
        gc.fillRoundRect(x, y, width, height, 3, 3);

        // HP fill color according to ratio
        Color hpColor;
        if (ratio > 0.6) {
            hpColor = Color.web("#22c55e"); // Green
        } else if (ratio > 0.25) {
            hpColor = Color.web("#f59e0b"); // Amber
        } else {
            hpColor = Color.web("#ef4444"); // Red
        }

        // Active HP bar
        if (ratio > 0) {
            gc.setFill(hpColor);
            gc.fillRoundRect(x, y, Math.max(2, width * ratio), height, 3, 3);
        }

        // Sleek metallic border
        gc.setStroke(Color.web("#475569", 0.8));
        gc.setLineWidth(0.8);
        gc.strokeRoundRect(x - 1, y - 1, width + 2, height + 2, 4, 4);
    }
}