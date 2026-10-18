package com.towerdefense.render;

import com.towerdefense.config.GameConfig;
import com.towerdefense.map.GameMap;
import com.towerdefense.map.TileType;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class MapRenderer {

    // ── Sci-Fi Dark Slate Terrain Palette ──
    private static final Color VOID_BG          = Color.web("#090d16");
    private static final Color TILE_DARK        = Color.web("#0e1524");
    private static final Color TILE_LIGHT       = Color.web("#121b2d");
    private static final Color GRID_LINE        = Color.web("#1e293b", 0.60);
    private static final Color GRID_DOT         = Color.web("#38bdf8", 0.35);

    // ── Path / Energy Runway Palette ──
    private static final Color PATH_BASE        = Color.web("#172033");
    private static final Color PATH_INNER       = Color.web("#1c273e");
    private static final Color PATH_RAIL_GLOW   = Color.web("#38bdf8", 0.30);
    private static final Color PATH_RAIL        = Color.web("#0ea5e9");
    private static final Color PATH_CENTERLINE  = Color.web("#38bdf8", 0.25);
    private static final Color PATH_CHEVRON     = Color.web("#60a5fa", 0.18);

    // ── Tower Platform (Tactical Defense Matrix) ──
    private static final Color PAD_SHADOW       = Color.web("#020617", 0.85);
    private static final Color PAD_BASE         = Color.web("#1b2434");
    private static final Color PAD_SURFACE      = Color.web("#222f44");
    private static final Color PAD_BORDER       = Color.web("#334155");
    private static final Color PAD_LED          = Color.web("#22c55e");
    private static final Color PAD_LED_GLOW     = Color.web("#22c55e", 0.25);
    private static final Color PAD_CYAN         = Color.web("#38bdf8", 0.70);

    // ── Fonts ──
    private static final Font FONT_BADGE = Font.font("Consolas", FontWeight.BOLD, 10);
    private static final Font FONT_CODE  = Font.font("Consolas", FontWeight.NORMAL, 8);

    public void render(GameMap map, GraphicsContext gc) {
        int rows = map.getRows();
        int cols = map.getCols();
        int ts   = GameMap.TILE_SIZE;

        // ── Pass 1: Dark Slate Void & Grid Background ──
        gc.setFill(VOID_BG);
        gc.fillRect(0, 0, cols * ts, rows * ts);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = c * ts;
                double y = r * ts;
                TileType tile = map.getTile(r, c);

                if (tile == TileType.BLOCKED || tile == TileType.BUILDABLE) {
                    boolean even = (r + c) % 2 == 0;
                    gc.setFill(even ? TILE_DARK : TILE_LIGHT);
                    gc.fillRect(x + 0.5, y + 0.5, ts - 1.0, ts - 1.0);

                    // Micro intersection dot at tile corners
                    gc.setFill(GRID_DOT);
                    gc.fillRect(x - 1, y - 1, 2, 2);
                }
            }
        }

        // ── Pass 2: Path Floor (Runway Surface) ──
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map.getTile(r, c) == TileType.PATH) {
                    renderPathTile(gc, map, r, c, c * ts, r * ts, ts);
                }
            }
        }

        // ── Pass 3: Glowing Neon Energy Rails ──
        renderPathRails(gc, map, rows, cols, ts);

        // ── Pass 4: Tactical Tower Platforms (64x64) ──
        renderTowerPlatforms(gc, map, ts);

        // ── Pass 5: Spawn Gate & Base Core ──
        renderSpawnAndBase(gc, map, ts);
    }

    // ══════════════════════════════════════════════════════════
    //  PATH RENDERING (CYBER RUNWAY)
    // ══════════════════════════════════════════════════════════

    private void renderPathTile(GraphicsContext gc, GameMap map, int r, int c, double x, double y, int ts) {
        // Dark metallic runner base
        gc.setFill(PATH_BASE);
        gc.fillRect(x, y, ts, ts);

        // Inner plate
        gc.setFill(PATH_INNER);
        gc.fillRect(x + 2, y + 2, ts - 4, ts - 4);

        // Subtle guide line in center
        gc.setStroke(PATH_CENTERLINE);
        gc.setLineWidth(1.0);
        gc.setLineDashes(4, 6);

        // Check orientation of path
        boolean horizontal = (c > 0 && map.getTile(r, c - 1) == TileType.PATH)
                || (c < map.getCols() - 1 && map.getTile(r, c + 1) == TileType.PATH);
        boolean vertical = (r > 0 && map.getTile(r - 1, c) == TileType.PATH)
                || (r < map.getRows() - 1 && map.getTile(r + 1, c) == TileType.PATH);

        if (horizontal && !vertical) {
            gc.strokeLine(x, y + ts / 2.0, x + ts, y + ts / 2.0);
        } else if (vertical && !horizontal) {
            gc.strokeLine(x + ts / 2.0, y, x + ts / 2.0, y + ts);
        } else {
            // Corner / Intersection
            gc.strokeLine(x, y + ts / 2.0, x + ts, y + ts / 2.0);
            gc.strokeLine(x + ts / 2.0, y, x + ts / 2.0, y + ts);
        }
        gc.setLineDashes(); // reset
    }

    private void renderPathRails(GraphicsContext gc, GameMap map, int rows, int cols, int ts) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map.getTile(r, c) == TileType.PATH) {
                    double x = c * ts;
                    double y = r * ts;

                    // Top rail
                    if (r > 0 && map.getTile(r - 1, c) != TileType.PATH) {
                        drawNeonRail(gc, x, y, x + ts, y, true);
                    }
                    // Bottom rail
                    if (r < rows - 1 && map.getTile(r + 1, c) != TileType.PATH) {
                        drawNeonRail(gc, x, y + ts, x + ts, y + ts, true);
                    }
                    // Left rail
                    if (c > 0 && map.getTile(r, c - 1) != TileType.PATH) {
                        drawNeonRail(gc, x, y, x, y + ts, false);
                    }
                    // Right rail
                    if (c < cols - 1 && map.getTile(r, c + 1) != TileType.PATH) {
                        drawNeonRail(gc, x + ts, y, x + ts, y + ts, false);
                    }
                }
            }
        }
    }

    private void drawNeonRail(GraphicsContext gc, double x1, double y1, double x2, double y2, boolean isHorizontal) {
        // Outer glow
        gc.setStroke(PATH_RAIL_GLOW);
        gc.setLineWidth(4.0);
        gc.strokeLine(x1, y1, x2, y2);

        // Sharp neon core
        gc.setStroke(PATH_RAIL);
        gc.setLineWidth(1.5);
        gc.strokeLine(x1, y1, x2, y2);
    }

    // ══════════════════════════════════════════════════════════
    //  TACTICAL TOWER PLATFORMS (64x64 HOLOGRAPHIC PADS)
    // ══════════════════════════════════════════════════════════

    private void renderTowerPlatforms(GraphicsContext gc, GameMap map, int ts) {
        int rows = map.getRows();
        int cols = map.getCols();
        int blockSize = GameConfig.TOWER_SIZE_IN_TILES;
        double padSize = blockSize * ts; // 64x64

        for (int r = 0; r < rows - blockSize + 1; r++) {
            for (int c = 0; c < cols - blockSize + 1; c++) {
                int[] block = map.findBlockContaining(r, c);
                if (block != null && block[0] == r && block[1] == c) {
                    double bx = c * ts;
                    double by = r * ts;

                    // Drop shadow
                    gc.setFill(PAD_SHADOW);
                    gc.fillRoundRect(bx + 3, by + 4, padSize - 6, padSize - 6, 8, 8);

                    // Deep carbon slab
                    gc.setFill(PAD_BASE);
                    gc.fillRoundRect(bx + 2, by + 2, padSize - 4, padSize - 4, 8, 8);

                    // Surface plate
                    gc.setFill(PAD_SURFACE);
                    gc.fillRoundRect(bx + 5, by + 5, padSize - 10, padSize - 10, 6, 6);

                    // Border
                    gc.setStroke(PAD_BORDER);
                    gc.setLineWidth(1.2);
                    gc.strokeRoundRect(bx + 2, by + 2, padSize - 4, padSize - 4, 8, 8);

                    // Tactical LED Corner Brackets: ┌ ┐ └ ┘
                    gc.setStroke(PAD_LED_GLOW);
                    gc.setLineWidth(4.0);
                    strokeBrackets(gc, bx, by, padSize);

                    gc.setStroke(PAD_LED);
                    gc.setLineWidth(1.8);
                    strokeBrackets(gc, bx, by, padSize);

                    // Holographic Reticle in center
                    double cx = bx + padSize / 2.0;
                    double cy = by + padSize / 2.0;

                    gc.setStroke(PAD_CYAN);
                    gc.setLineWidth(1.2);
                    gc.strokeOval(cx - 12, cy - 12, 24, 24);

                    gc.setStroke(Color.web("#38bdf8", 0.40));
                    gc.setLineWidth(1.0);
                    gc.strokeLine(cx - 16, cy, cx - 13, cy);
                    gc.strokeLine(cx + 13, cy, cx + 16, cy);
                    gc.strokeLine(cx, cy - 16, cx, cy - 13);
                    gc.strokeLine(cx, cy + 13, cx, cy + 16);

                    // Badge text
                    gc.setFont(FONT_BADGE);
                    gc.setFill(PAD_LED);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText("POD", cx, cy + 18);
                }
            }
        }
    }

    private void strokeBrackets(GraphicsContext gc, double bx, double by, double size) {
        double len = 10;
        double ins = 7;

        // Top-Left ┌
        gc.strokeLine(bx + ins, by + ins, bx + ins + len, by + ins);
        gc.strokeLine(bx + ins, by + ins, bx + ins, by + ins + len);

        // Top-Right ┐
        gc.strokeLine(bx + size - ins, by + ins, bx + size - ins - len, by + ins);
        gc.strokeLine(bx + size - ins, by + ins, bx + size - ins, by + ins + len);

        // Bottom-Left └
        gc.strokeLine(bx + ins, by + size - ins, bx + ins + len, by + size - ins);
        gc.strokeLine(bx + ins, by + size - ins, bx + ins, by + size - ins - len);

        // Bottom-Right ┘
        gc.strokeLine(bx + size - ins, by + size - ins, bx + size - ins - len, by + size - ins);
        gc.strokeLine(bx + size - ins, by + size - ins, bx + size - ins, by + size - ins - len);
    }

    // ══════════════════════════════════════════════════════════
    //  SPAWN GATE & BASE CORE
    // ══════════════════════════════════════════════════════════

    private void renderSpawnAndBase(GraphicsContext gc, GameMap map, int ts) {
        if (map.getWaypoints() == null || map.getWaypoints().isEmpty()) return;

        // ── HOSTILE SPAWN PORTAL (First waypoint) ──
        var firstWp = map.getWaypoints().get(0);
        double sx = firstWp.x();
        double sy = firstWp.y();

        // Pulsing Crimson Core
        gc.setFill(Color.web("#e11d48", 0.25));
        gc.fillOval(sx - 20, sy - 20, 40, 40);

        gc.setStroke(Color.web("#f43f5e"));
        gc.setLineWidth(2.0);
        gc.strokeOval(sx - 12, sy - 12, 24, 24);

        gc.setFill(Color.web("#fb7185"));
        gc.fillOval(sx - 5, sy - 5, 10, 10);

        // HUD Badge
        gc.setFill(Color.web("#881337"));
        gc.fillRoundRect(sx - 24, sy - 28, 48, 14, 3, 3);
        gc.setStroke(Color.web("#f43f5e"));
        gc.setLineWidth(1.0);
        gc.strokeRoundRect(sx - 24, sy - 28, 48, 14, 3, 3);

        gc.setFont(FONT_BADGE);
        gc.setFill(Color.web("#ffe4e6"));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("WARP-IN", sx, sy - 17);

        // ── BASE DEFENSE CORE (Last waypoint) ──
        var lastWp = map.getWaypoints().get(map.getWaypoints().size() - 1);
        double bx = lastWp.x();
        double by = lastWp.y();

        // Cyan Energy Field
        gc.setFill(Color.web("#06b6d4", 0.25));
        gc.fillOval(bx - 22, by - 22, 44, 44);

        gc.setStroke(Color.web("#06b6d4"));
        gc.setLineWidth(2.0);
        gc.strokeOval(bx - 14, bx != 0 ? by - 14 : 0, 28, 28);

        // Diamond Energy Crystal
        gc.setFill(Color.web("#22d3ee"));
        gc.fillPolygon(
            new double[]{ bx, bx + 9, bx, bx - 9 },
            new double[]{ by - 12, by, by + 12, by },
            4
        );

        // HUD Badge
        gc.setFill(Color.web("#164e63"));
        gc.fillRoundRect(bx - 24, by - 28, 48, 14, 3, 3);
        gc.setStroke(Color.web("#06b6d4"));
        gc.setLineWidth(1.0);
        gc.strokeRoundRect(bx - 24, by - 28, 48, 14, 3, 3);

        gc.setFont(FONT_BADGE);
        gc.setFill(Color.web("#cffafe"));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("CORE-HQ", bx, by - 17);
    }

    // ══════════════════════════════════════════════════════════
    //  HOVER HIGHLIGHT & RANGE INDICATOR
    // ══════════════════════════════════════════════════════════

    public void renderHoverAndRange(GameMap map, GraphicsContext gc, double mouseX, double mouseY, com.towerdefense.entity.tower.TowerType selectedTower) {
        if (mouseX < 0 || mouseY < 0) return;

        int row = map.rowFromY(mouseY);
        int col = map.colFromX(mouseX);
        int[] block = map.findBlockContaining(row, col);

        double range = switch (selectedTower != null ? selectedTower : com.towerdefense.entity.tower.TowerType.BASIC) {
            case BASIC -> 170;
            case SPREAD -> 160;
            case SLOW -> 180;
            case EXPLOSIVE -> 165;
        };

        if (block != null) {
            int ts = GameMap.TILE_SIZE;
            double bx = block[1] * ts;
            double by = block[0] * ts;
            double padSize = GameConfig.TOWER_SIZE_IN_TILES * ts;
            double cx = bx + padSize / 2.0;
            double cy = by + padSize / 2.0;

            boolean isFree = map.isBlockFree(block);
            Color ringColor = isFree ? Color.web("#38bdf8") : Color.web("#f59e0b");
            Color fillColor = isFree ? Color.web("#38bdf8", 0.08) : Color.web("#f59e0b", 0.08);

            // 1. Holographic Range Circle (snapped to pod)
            gc.setFill(fillColor);
            gc.fillOval(cx - range, cy - range, range * 2, range * 2);

            gc.setStroke(ringColor);
            gc.setLineWidth(1.5);
            gc.setLineDashes(6, 6);
            gc.strokeOval(cx - range, cy - range, range * 2, range * 2);
            gc.setLineDashes(); // reset

            // 2. Pod Hover Frame
            Color frameColor = isFree ? Color.web("#4ade80") : Color.web("#f87171");
            gc.setStroke(frameColor);
            gc.setLineWidth(2.5);
            gc.strokeRoundRect(bx + 1, by + 1, padSize - 2, padSize - 2, 8, 8);

            // Status label
            gc.setFont(FONT_BADGE);
            gc.setFill(frameColor);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(isFree ? "READY" : "OCCUPIED", cx, by - 6);
        }
    }
}