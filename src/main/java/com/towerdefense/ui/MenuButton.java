package com.towerdefense.ui;

import java.util.Objects;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

final class MenuButton {

    static final double WIDTH = 280;
    static final double HEIGHT = 48;
    static final double GAP = 16;

    enum Variant {
        PRIMARY,
        SECONDARY,
        DESTRUCTIVE
    }

    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final String label;
    private final Variant variant;
    private final Runnable action;

    MenuButton(
            double x,
            double y,
            double width,
            double height,
            String label,
            Variant variant,
            Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = Objects.requireNonNull(label);
        this.variant = Objects.requireNonNull(variant);
        this.action = Objects.requireNonNull(action);
    }

    void render(GraphicsContext graphics) {
        graphics.save();
        graphics.setFill(switch (variant) {
            case PRIMARY -> Color.rgb(40, 115, 180);
            case SECONDARY -> Color.rgb(60, 60, 60);
            case DESTRUCTIVE -> Color.rgb(160, 67, 55);
        });
        graphics.fillRoundRect(x, y, width, height, 10, 10);
        graphics.setFill(Color.WHITE);
        graphics.setFont(Font.font(18));
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.fillText(label, x + width / 2.0, y + height / 2.0);
        graphics.restore();
    }

    boolean handleClick(double clickX, double clickY) {
        if (clickX < x || clickX > x + width || clickY < y || clickY > y + height) {
            return false;
        }

        action.run();
        return true;
    }
}
