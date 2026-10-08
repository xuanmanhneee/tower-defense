package com.towerdefense.render;

import com.towerdefense.core.Vector2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DiscRenderer extends RenderBehaviour {

    private static final double FILL_OPACITY = 0.30;
    private static final double STROKE_WIDTH = 2;

    private final double radius;
    private Color color;
    private Color fill;
    private final boolean solid;

    public DiscRenderer(RenderLayer layer, Color color, double radius) {
        this(layer, color, radius, false);
    }

    public DiscRenderer(RenderLayer layer, Color color, double radius, boolean solid) {
        super(layer);
        this.radius = radius;
        this.solid = solid;
        setColor(color);
    }

    /** Cho phép đổi màu lúc chạy, ví dụ địch bị slow chuyển viền sang cyan. */
    public void setColor(Color color) {
        this.color = color;
        this.fill = solid ? color : color.deriveColor(0, 1, 1, FILL_OPACITY);
    }

    @Override
    public void render(GraphicsContext graphics) {
        Vector2 p = gameObject.getTransform().getPosition();

        // Viền nằm giữa mép nên thụt vào nửa nét để không vượt bán kính collider
        double r = radius - STROKE_WIDTH / 2;
        double x = p.x() - r;
        double y = p.y() - r;
        double d = r * 2;

        graphics.setFill(fill);
        graphics.fillOval(x, y, d, d);

        if (!solid) {
            graphics.setStroke(color);
            graphics.setLineWidth(STROKE_WIDTH);
            graphics.strokeOval(x, y, d, d);
        }
    }
}