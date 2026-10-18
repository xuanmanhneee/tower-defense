package com.towerdefense.render;

import com.towerdefense.behaviour.PathFollower;
import com.towerdefense.behaviour.SpeedModifiers;
import com.towerdefense.core.Vector2;
import com.towerdefense.entity.enemy.EnemyType;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EnemyRenderer extends RenderBehaviour {

    private final EnemyType type;
    private double currentAngle = 0;

    public EnemyRenderer(EnemyType type) {
        super(RenderLayer.ENEMY);
        this.type = type;
    }

    public EnemyType getType() {
        return type;
    }

    @Override
    public void render(GraphicsContext gc) {
        Vector2 pos = gameObject.getTransform().getPosition();
        double x = pos.x();
        double y = pos.y();

        // Direction tracking from PathFollower
        PathFollower follower = gameObject.getBehaviour(PathFollower.class);
        if (follower != null) {
            Vector2 dir = follower.getCurrentDirection();
            if (dir.lengthSquared() > 0.01) {
                currentAngle = Math.toDegrees(Math.atan2(dir.y(), dir.x()));
            }
        }

        // Slow effect aura check
        SpeedModifiers speedMod = gameObject.getBehaviour(SpeedModifiers.class);
        boolean isSlowed = (speedMod != null && speedMod.getMultiplier() < 0.95);

        if (isSlowed) {
            renderSlowAura(gc, x, y);
        }

        // Draw rotated enemy
        gc.save();
        gc.translate(x, y);
        gc.rotate(currentAngle);

        switch (type) {
            case SCOUT -> renderScout(gc);
            case GRUNT -> renderGrunt(gc);
            case TANK  -> renderTank(gc);
        }

        gc.restore();
    }

    // ══════════════════════════════════════════════════════════
    //  SCOUT (FAST INTERCEPTOR DART)
    // ══════════════════════════════════════════════════════════

    private void renderScout(GraphicsContext gc) {
        // Thruster exhaust trail
        gc.setFill(Color.web("#38bdf8", 0.6));
        gc.fillPolygon(
            new double[]{ -7, -13, -7 },
            new double[]{ -3, 0, 3 },
            3
        );

        // Supersonic Dart Hull (Forward is +X)
        gc.setFill(Color.web("#eab308"));
        gc.fillPolygon(
            new double[]{ 10, -7, -4, -7 },
            new double[]{ 0, -8, 0, 8 },
            4
        );

        // Hull Edge Highlight
        gc.setStroke(Color.web("#fef08a"));
        gc.setLineWidth(1.0);
        gc.strokePolygon(
            new double[]{ 10, -7, -4, -7 },
            new double[]{ 0, -8, 0, 8 },
            4
        );

        // Cyber Cockpit
        gc.setFill(Color.web("#0284c7"));
        gc.fillOval(0, -2, 5, 4);

        gc.setFill(Color.web("#38bdf8"));
        gc.fillOval(2, -1.5, 2.5, 3);
    }

    // ══════════════════════════════════════════════════════════
    //  GRUNT (RAZOR COMBAT DRONE)
    // ══════════════════════════════════════════════════════════

    private void renderGrunt(GraphicsContext gc) {
        // Drop shadow
        gc.setFill(Color.web("#020617", 0.4));
        gc.fillOval(-10, -9, 20, 18);

        // 4 Swept Wing Blades (Forward is +X)
        gc.setFill(Color.web("#e11d48"));
        // Top forward wing
        gc.fillPolygon(new double[]{ 2, 10, -2 }, new double[]{ -2, -11, -5 }, 3);
        // Bottom forward wing
        gc.fillPolygon(new double[]{ 2, 10, -2 }, new double[]{ 2, 11, 5 }, 3);
        // Top rear stabilizer
        gc.fillPolygon(new double[]{ -4, -9, -4 }, new double[]{ -2, -8, -5 }, 3);
        // Bottom rear stabilizer
        gc.fillPolygon(new double[]{ -4, -9, -4 }, new double[]{ 2, 8, 5 }, 3);

        // Armored Center Pod
        gc.setFill(Color.web("#1e293b"));
        gc.fillOval(-7, -7, 14, 14);

        gc.setStroke(Color.web("#f43f5e"));
        gc.setLineWidth(1.2);
        gc.strokeOval(-7, -7, 14, 14);

        // Glowing Optical Eye (Forward looking)
        gc.setFill(Color.web("#fda4af"));
        gc.fillOval(0, -3, 6, 6);

        gc.setFill(Color.WHITE);
        gc.fillOval(2, -2, 3, 3);
    }

    // ══════════════════════════════════════════════════════════
    //  TANK (HEAVY ARMORED JUGGERNAUT)
    // ══════════════════════════════════════════════════════════

    private void renderTank(GraphicsContext gc) {
        // Drop shadow
        gc.setFill(Color.web("#020617", 0.5));
        gc.fillRoundRect(-16, -14, 32, 28, 6, 6);

        // 4 Corner Track / Tread Pods
        gc.setFill(Color.web("#1e293b"));
        gc.fillRoundRect(-14, -13, 8, 6, 2, 2);
        gc.fillRoundRect(6, -13, 8, 6, 2, 2);
        gc.fillRoundRect(-14, 7, 8, 6, 2, 2);
        gc.fillRoundRect(6, 7, 8, 6, 2, 2);

        // Main Heavy Armor Hull (Octagonal / Beveled Slab)
        gc.setFill(Color.web("#334155"));
        gc.fillPolygon(
            new double[]{ -12, 8, 14, 14, 8, -12, -14, -14 },
            new double[]{ -11, -11, -6, 6, 11, 11, 6, -6 },
            8
        );

        // Reinforced Hazard Plates (Orange / Crimson)
        gc.setStroke(Color.web("#f97316"));
        gc.setLineWidth(1.5);
        gc.strokePolygon(
            new double[]{ -12, 8, 14, 14, 8, -12, -14, -14 },
            new double[]{ -11, -11, -6, 6, 11, 11, 6, -6 },
            8
        );

        // Heavy Ram Prow in front
        gc.setFill(Color.web("#475569"));
        gc.fillRect(10, -5, 5, 10);
        gc.setStroke(Color.web("#f97316"));
        gc.setLineWidth(1.0);
        gc.strokeRect(10, -5, 5, 10);

        // Central Reinforced Reactor Core
        gc.setFill(Color.web("#1e293b"));
        gc.fillOval(-7, -7, 14, 14);

        gc.setFill(Color.web("#ea580c"));
        gc.fillOval(-4, -4, 8, 8);

        gc.setFill(Color.web("#fed7aa"));
        gc.fillOval(-2, -2, 4, 4);
    }

    // ══════════════════════════════════════════════════════════
    //  SLOW STATUS AURA
    // ══════════════════════════════════════════════════════════

    private void renderSlowAura(GraphicsContext gc, double x, double y) {
        // Icy Cyan Ring
        gc.setStroke(Color.web("#38bdf8", 0.55));
        gc.setLineWidth(2.0);
        gc.setLineDashes(4, 4);
        gc.strokeOval(x - 17, y - 17, 34, 34);
        gc.setLineDashes(); // reset

        // Frost crystal glints
        gc.setFill(Color.web("#bae6fd", 0.75));
        gc.fillOval(x - 15, y - 5, 3, 3);
        gc.fillOval(x + 12, y + 4, 3, 3);
        gc.fillOval(x - 4, y - 14, 3, 3);
        gc.fillOval(x + 3, y + 12, 3, 3);
    }
}
