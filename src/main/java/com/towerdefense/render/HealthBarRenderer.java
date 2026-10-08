package com.towerdefense.render;

import com.towerdefense.behaviour.Health;
import com.towerdefense.core.Vector2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthBarRenderer extends RenderBehaviour {

    private final double width;
    private final double height;
    private final double offsetY;

    public HealthBarRenderer(
            double width,
            double height,
            double offsetY) {

        super(RenderLayer.OVERLAY);

        this.width = width;
        this.height = height;
        this.offsetY = offsetY;
    }

    @Override
    public void render(GraphicsContext graphics) {
        Health health = gameObject.getBehaviour(Health.class);

        if (health == null || health.isDead()) {
            return;
        }

        Vector2 position = gameObject.getTransform().getPosition();

        double x = position.x() - width / 2;
        double y = position.y() - offsetY;

        graphics.setFill(Color.DARKRED);
        graphics.fillRect(x, y, width, height);

        graphics.setFill(Color.LIMEGREEN);
        graphics.fillRect(
                x,
                y,
                width * health.getRatio(),
                height);
    }
}