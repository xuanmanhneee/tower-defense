package com.towerdefense.render;

import com.towerdefense.behaviour.Targeting;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;
import com.towerdefense.entity.tower.TowerType;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TowerRenderer extends RenderBehaviour {

    private final TowerType type;

    // Palette per tower type
    private final Color primaryColor;
    private final Color glowColor;
    private final Color barrelColor;

    // Smooth rotation angle
    private double currentAngle = 0;

    public TowerRenderer(TowerType type) {
        super(RenderLayer.TOWER);
        this.type = type;

        switch (type) {
            case BASIC -> {
                this.primaryColor = Color.web("#facc15"); // Amber Gold
                this.glowColor    = Color.web("#fde047", 0.40);
                this.barrelColor  = Color.web("#1e293b");
            }
            case SPREAD -> {
                this.primaryColor = Color.web("#22c55e"); // Emerald
                this.glowColor    = Color.web("#4ade80", 0.40);
                this.barrelColor  = Color.web("#1e293b");
            }
            case SLOW -> {
                this.primaryColor = Color.web("#38bdf8"); // Ice Cyan
                this.glowColor    = Color.web("#7dd3fc", 0.45);
                this.barrelColor  = Color.web("#0f172a");
            }
            case EXPLOSIVE -> {
                this.primaryColor = Color.web("#ef4444"); // Crimson
                this.glowColor    = Color.web("#f87171", 0.40);
                this.barrelColor  = Color.web("#18181b");
            }
            default -> {
                this.primaryColor = Color.WHITE;
                this.glowColor    = Color.web("#ffffff", 0.3);
                this.barrelColor  = Color.GRAY;
            }
        }
    }

    public TowerType getType() {
        return type;
    }

    @Override
    public void render(GraphicsContext gc) {
        Vector2 pos = gameObject.getTransform().getPosition();
        double x = pos.x();
        double y = pos.y();

        // Target tracking
        Targeting targeting = gameObject.getBehaviour(Targeting.class);
        if (targeting != null) {
            GameObject target = targeting.getCurrentTarget();
            if (target != null && target.isAlive()) {
                Vector2 targetPos = target.getTransform().getPosition();
                double dx = targetPos.x() - x;
                double dy = targetPos.y() - y;
                currentAngle = Math.toDegrees(Math.atan2(dy, dx));
            }
        }

        // ── 1. Stationary Turret Base (Chassis) ──
        renderChassis(gc, x, y);

        // ── 2. Rotating Turret Head & Barrels ──
        gc.save();
        gc.translate(x, y);
        gc.rotate(currentAngle);

        switch (type) {
            case BASIC     -> renderBasicTurret(gc);
            case SPREAD    -> renderSpreadTurret(gc);
            case SLOW      -> renderSlowTurret(gc);
            case EXPLOSIVE -> renderExplosiveTurret(gc);
        }

        gc.restore();
    }

    // ══════════════════════════════════════════════════════════
    //  STATIONARY CHASSIS
    // ══════════════════════════════════════════════════════════

    private void renderChassis(GraphicsContext gc, double x, double y) {
        // Drop shadow
        gc.setFill(Color.web("#020617", 0.50));
        gc.fillOval(x - 17, y - 15, 34, 34);

        // Outer heavy armor ring (Octagonal / circle base)
        gc.setFill(Color.web("#1e293b"));
        gc.fillOval(x - 15, y - 15, 30, 30);

        gc.setStroke(Color.web("#475569"));
        gc.setLineWidth(1.5);
        gc.strokeOval(x - 15, y - 15, 30, 30);

        // Glowing tech ring
        gc.setStroke(glowColor);
        gc.setLineWidth(3.0);
        gc.strokeOval(x - 11, y - 11, 22, 22);

        gc.setStroke(primaryColor);
        gc.setLineWidth(1.2);
        gc.strokeOval(x - 11, y - 11, 22, 22);

        // 4 Mechanical Mount Bolts
        gc.setFill(Color.web("#94a3b8"));
        gc.fillOval(x - 14, y - 2, 3, 3);
        gc.fillOval(x + 11, y - 2, 3, 3);
        gc.fillOval(x - 2, y - 14, 3, 3);
        gc.fillOval(x - 2, y + 11, 3, 3);
    }

    // ══════════════════════════════════════════════════════════
    //  BASIC TURRET (DUAL GATLING / AUTOCANNON)
    // ══════════════════════════════════════════════════════════

    private void renderBasicTurret(GraphicsContext gc) {
        // Dual Gun Barrels
        gc.setFill(barrelColor);
        gc.fillRect(0, -6, 17, 3.5);  // Top barrel
        gc.fillRect(0, 2.5, 17, 3.5); // Bottom barrel

        // Glowing Muzzle Tips
        gc.setFill(primaryColor);
        gc.fillRect(15, -6, 3, 3.5);
        gc.fillRect(15, 2.5, 3, 3.5);

        // Turret Cap (Armor Dome)
        gc.setFill(Color.web("#334155"));
        gc.fillOval(-9, -9, 18, 18);

        gc.setStroke(primaryColor);
        gc.setLineWidth(1.2);
        gc.strokeOval(-9, -9, 18, 18);

        // Center Optical Visor
        gc.setFill(primaryColor);
        gc.fillOval(1, -3, 6, 6);
    }

    // ══════════════════════════════════════════════════════════
    //  SPREAD TURRET (TRIPLE SCATTER CANNON)
    // ══════════════════════════════════════════════════════════

    private void renderSpreadTurret(GraphicsContext gc) {
        // Center Barrel
        gc.setFill(barrelColor);
        gc.fillRect(0, -2, 17, 4);

        // Left Diverging Barrel (-20 deg)
        gc.save();
        gc.rotate(-22);
        gc.fillRect(0, -2, 15, 3.5);
        gc.setFill(primaryColor);
        gc.fillRect(13, -2, 3, 3.5);
        gc.restore();

        // Right Diverging Barrel (+20 deg)
        gc.save();
        gc.rotate(22);
        gc.setFill(barrelColor);
        gc.fillRect(0, -2, 15, 3.5);
        gc.setFill(primaryColor);
        gc.fillRect(13, -2, 3, 3.5);
        gc.restore();

        // Center muzzle tip
        gc.setFill(primaryColor);
        gc.fillRect(15, -2, 3, 4);

        // Heavy Angular Turret Dome (Hexagon)
        gc.setFill(Color.web("#1e293b"));
        gc.fillPolygon(
            new double[]{ -8, 2, 8, 2, -8, -10 },
            new double[]{ -10, -9, 0, 9, 10, 0 },
            6
        );

        gc.setStroke(primaryColor);
        gc.setLineWidth(1.2);
        gc.strokePolygon(
            new double[]{ -8, 2, 8, 2, -8, -10 },
            new double[]{ -10, -9, 0, 9, 10, 0 },
            6
        );

        // Power Core
        gc.setFill(primaryColor);
        gc.fillOval(-4, -4, 8, 8);
    }

    // ══════════════════════════════════════════════════════════
    //  SLOW TURRET (CRYO / TESLA EMITTER)
    // ══════════════════════════════════════════════════════════

    private void renderSlowTurret(GraphicsContext gc) {
        // Dual Energy Emitter Prongs (Curved Antennae)
        gc.setStroke(Color.web("#64748b"));
        gc.setLineWidth(2.5);
        gc.strokeArc(0, -11, 18, 12, 0, 160, javafx.scene.shape.ArcType.OPEN);
        gc.strokeArc(0, -1, 18, 12, 200, 160, javafx.scene.shape.ArcType.OPEN);

        // Emitter Nodes
        gc.setFill(primaryColor);
        gc.fillOval(14, -8, 4, 4);
        gc.fillOval(14, 4, 4, 4);

        // Dish reflector
        gc.setFill(Color.web("#1e293b"));
        gc.fillArc(-8, -10, 12, 20, 90, 180, javafx.scene.shape.ArcType.ROUND);

        gc.setStroke(Color.web("#475569"));
        gc.setLineWidth(1.0);
        gc.strokeArc(-8, -10, 12, 20, 90, 180, javafx.scene.shape.ArcType.ROUND);

        // Pulsing Ice Plasma Core
        gc.setFill(glowColor);
        gc.fillOval(-3, -6, 12, 12);

        gc.setFill(primaryColor);
        gc.fillOval(-1, -4, 8, 8);

        gc.setFill(Color.WHITE);
        gc.fillOval(1, -2, 4, 4);
    }

    // ══════════════════════════════════════════════════════════
    //  EXPLOSIVE TURRET (HEAVY SIEGE MORTAR / CANNON)
    // ══════════════════════════════════════════════════════════

    private void renderExplosiveTurret(GraphicsContext gc) {
        // Massive Heavy Cannon Barrel
        gc.setFill(barrelColor);
        gc.fillRect(-2, -4.5, 18, 9);

        // Reinforced Muzzle Brake
        gc.setFill(Color.web("#27272a"));
        gc.fillRect(12, -6, 5, 12);

        gc.setStroke(primaryColor);
        gc.setLineWidth(1.2);
        gc.strokeRect(12, -6, 5, 12);

        // Barrel Heat Vent slots
        gc.setFill(primaryColor);
        gc.fillRect(3, -3, 3, 6);
        gc.fillRect(8, -3, 2, 6);

        // Heavy Reinforced Blast Shield
        gc.setFill(Color.web("#3f3f46"));
        gc.fillRect(-10, -9, 11, 18);

        gc.setStroke(primaryColor);
        gc.setLineWidth(1.5);
        gc.strokeRect(-10, -9, 11, 18);

        // Core Breech Hatch
        gc.setFill(Color.web("#18181b"));
        gc.fillOval(-8, -4, 8, 8);

        gc.setFill(primaryColor);
        gc.fillOval(-6, -2, 4, 4);
    }
}
