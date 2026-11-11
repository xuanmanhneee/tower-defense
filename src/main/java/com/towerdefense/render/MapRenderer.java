package com.towerdefense.render;

import com.towerdefense.map.GameMap;
import com.towerdefense.map.TileType;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapRenderer {

    private static final Color GROUND_A = Color.web("#18231e");
    private static final Color GROUND_B = Color.web("#1b2822");
    private static final Color PATH_A = Color.web("#a97748");
    private static final Color PATH_B = Color.web("#ae7d4d");
    private static final Color PATH_EDGE = Color.web("#d1a36d");
    private static final Color BUILD_PAD = Color.web("#214b37");
    private static final Color BUILD_BORDER = Color.web("#408761");

    public void render(GameMap map, GraphicsContext graphics) {
        int size = GameMap.TILE_SIZE;

        for (int row = 0; row < map.getRows(); row++) {
            for (int col = 0; col < map.getCols(); col++) {
                double x = col * size;
                double y = row * size;
                graphics.setFill((row + col) % 2 == 0 ? GROUND_A : GROUND_B);
                graphics.fillRect(x, y, size, size);

                TileType tile = map.getTile(row, col);
                if (tile == TileType.PATH) {
                    drawPathTile(graphics, row, col, x, y, size);
                } else if (tile == TileType.BUILDABLE) {
                    drawBuildTile(graphics, x, y, size);
                }
            }
        }

        drawPathEdges(map, graphics, size);
    }

    private void drawPathTile(
            GraphicsContext graphics,
            int row,
            int col,
            double x,
            double y,
            int size) {
        graphics.setFill((row + col) % 2 == 0 ? PATH_A : PATH_B);
        graphics.fillRect(x, y, size, size);
        graphics.setStroke(Color.rgb(72, 45, 27, 0.15));
        graphics.setLineWidth(1);
        graphics.strokeRect(x + 0.5, y + 0.5, size - 1, size - 1);
    }

    private void drawBuildTile(GraphicsContext graphics, double x, double y, int size) {
        graphics.setFill(BUILD_PAD);
        graphics.fillRoundRect(x + 3, y + 3, size - 6, size - 6, 5, 5);
        graphics.setStroke(BUILD_BORDER);
        graphics.setLineWidth(1);
        graphics.strokeRoundRect(x + 3.5, y + 3.5, size - 7, size - 7, 5, 5);
    }

    private void drawPathEdges(GameMap map, GraphicsContext graphics, int size) {
        graphics.setStroke(PATH_EDGE);
        graphics.setLineWidth(2);

        for (int row = 0; row < map.getRows(); row++) {
            for (int col = 0; col < map.getCols(); col++) {
                if (map.getTile(row, col) != TileType.PATH) {
                    continue;
                }

                double x = col * size;
                double y = row * size;
                if (map.getTile(row - 1, col) != TileType.PATH) {
                    graphics.strokeLine(x, y + 1, x + size, y + 1);
                }
                if (map.getTile(row + 1, col) != TileType.PATH) {
                    graphics.strokeLine(x, y + size - 1, x + size, y + size - 1);
                }
                if (map.getTile(row, col - 1) != TileType.PATH) {
                    graphics.strokeLine(x + 1, y, x + 1, y + size);
                }
                if (map.getTile(row, col + 1) != TileType.PATH) {
                    graphics.strokeLine(x + size - 1, y, x + size - 1, y + size);
                }
            }
        }
    }
}
