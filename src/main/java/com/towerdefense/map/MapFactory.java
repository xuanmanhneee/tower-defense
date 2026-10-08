package com.towerdefense.map;

import com.towerdefense.config.GameConfig;
import com.towerdefense.core.Vector2;

import java.util.ArrayList;
import java.util.List;

public final class MapFactory {

    private MapFactory() {
    }

    public static GameMap createLevel1Map() {
        TileType[][] grid = buildGrid(
            Level1MapData.ROWS,
            Level1MapData.COLS,
            Level1MapData.PATH_SEGMENTS,
            Level1MapData.BUILDABLE_BLOCKS
        );

        List<Vector2> waypoints = buildWaypoints(Level1MapData.PATH_SEGMENTS);

        return new GameMap(
            grid,
            waypoints,
            Level1MapData.BUILDABLE_BLOCKS,
            GameConfig.TOWER_SIZE_IN_TILES
        );
    }

    private static TileType[][] buildGrid(
            int rows,
            int cols,
            int[][] pathSegments,
            int[][] buildableBlocks) {

        TileType[][] grid = new TileType[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = TileType.BLOCKED;
            }
        }

        for (int[] segment : pathSegments) {
            drawPathSegment(grid, segment[0], segment[1], segment[2], segment[3]);
        }

        int blockSize = GameConfig.TOWER_SIZE_IN_TILES;
        for (int[] block : buildableBlocks) {
            markBuildableBlock(grid, block[0], block[1], blockSize);
        }

        return grid;
    }

    private static void drawPathSegment(
            TileType[][] grid,
            int fromRow, int fromCol,
            int toRow, int toCol) {

        if (fromRow == toRow) {
            int start = Math.min(fromCol, toCol);
            int end = Math.max(fromCol, toCol);
            for (int c = start; c <= end; c++) {
                setPath(grid, fromRow, c);
            }
        } else {
            int start = Math.min(fromRow, toRow);
            int end = Math.max(fromRow, toRow);
            for (int r = start; r <= end; r++) {
                setPath(grid, r, fromCol);
            }
        }
    }

    private static void setPath(TileType[][] grid, int row, int col) {
        if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
            grid[row][col] = TileType.PATH;
        }
    }

    private static void markBuildableBlock(TileType[][] grid, int row, int col, int size) {
        for (int dr = 0; dr < size; dr++) {
            for (int dc = 0; dc < size; dc++) {
                int r = row + dr;
                int c = col + dc;
                if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length
                        && grid[r][c] != TileType.PATH) {
                    grid[r][c] = TileType.BUILDABLE;
                }
            }
        }
    }

    private static List<Vector2> buildWaypoints(int[][] pathSegments) {
        List<Vector2> waypoints = new ArrayList<>();
        waypoints.add(tileCenter(pathSegments[0][0], pathSegments[0][1]));
        for (int[] segment : pathSegments) {
            waypoints.add(tileCenter(segment[2], segment[3]));
        }
        return waypoints;
    }

    private static Vector2 tileCenter(int row, int col) {
        return new Vector2(
            col * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2.0,
            row * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2.0
        );
    }
}