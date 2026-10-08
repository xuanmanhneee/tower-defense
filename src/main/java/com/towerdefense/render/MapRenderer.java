package com.towerdefense.render;

import com.towerdefense.map.GameMap;
import com.towerdefense.map.TileType;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapRenderer {

    public void render(GameMap map, GraphicsContext graphics) {
        for (int row = 0; row < map.getRows(); row++) {
            for (int col = 0; col < map.getCols(); col++) {

                TileType tile = map.getTile(row, col);

                graphics.setFill(getColor(tile));

                double x = col * GameMap.TILE_SIZE;
                double y = row * GameMap.TILE_SIZE;

                graphics.fillRect(
                        x,
                        y,
                        GameMap.TILE_SIZE,
                        GameMap.TILE_SIZE
                );
            }
        }
    }

    private Color getColor(TileType tile) {
        return switch (tile) {
            case PATH -> Color.SANDYBROWN;
            case BUILDABLE -> Color.FORESTGREEN;
            case BLOCKED -> Color.DIMGRAY;
        };
    }
}