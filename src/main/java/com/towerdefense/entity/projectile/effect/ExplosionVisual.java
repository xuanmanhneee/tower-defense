package com.towerdefense.entity.projectile.effect;

import com.towerdefense.core.Vector2;
import com.towerdefense.render.RenderBehaviour;
import com.towerdefense.render.RenderLayer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ExplosionVisual extends RenderBehaviour {
    private final double maxRadius;
    private final double duration;
    private double elapsed = 0;

    public ExplosionVisual(double maxRadius, double duration) {
        super(RenderLayer.OVERLAY);   // chỉnh theo enum RenderLayer thật
        this.maxRadius = maxRadius;
        this.duration = duration;
    }

    @Override
    public void update(double deltaTime) {
        elapsed += deltaTime;
        if (elapsed >= duration) {
            gameObject.destroy();
        }
    }

    @Override
    public void render(GraphicsContext graphics) {
        double t = Math.min(elapsed / duration, 1.0);
        double r = maxRadius * t;
        Vector2 p = gameObject.getTransform().getPosition();

        graphics.setGlobalAlpha(1.0 - t);
        graphics.setFill(Color.rgb(255, 140, 0, 0.5));
        graphics.fillOval(p.x() - r, p.y() - r, r * 2, r * 2);
        graphics.setStroke(Color.RED);
        graphics.setLineWidth(2);
        graphics.strokeOval(p.x() - r, p.y() - r, r * 2, r * 2);
        graphics.setGlobalAlpha(1.0);   // reset, nếu không mọi thứ vẽ sau bị mờ
    }

    @Override
    public void onReset() {
        elapsed = 0;   // phòng khi sau này object được pool tái sử dụng
    }
}