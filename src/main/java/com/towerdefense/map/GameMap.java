package com.towerdefense.map;

import com.towerdefense.core.Vector2;

import java.util.List;

public class GameMap {

    public static final int TILE_SIZE = com.towerdefense.config.GameConfig.TILE_SIZE;

    private final TileType[][] grid;
    private final boolean[][] occupied;
    private final List<Vector2> waypoints;
    private final int[][] buildableBlocks;
    private final int blockSize;

    public GameMap(
            TileType[][] grid,
            List<Vector2> waypoints,
            int[][] buildableBlocks,
            int blockSize) {

        this.grid = grid;
        this.occupied = new boolean[grid.length][grid[0].length];
        this.waypoints = List.copyOf(waypoints);
        this.buildableBlocks = buildableBlocks;
        this.blockSize = blockSize;
    }

    public TileType getTile(int row, int col) {
        if (row < 0
                || row >= grid.length
                || col < 0
                || col >= grid[0].length) {

            return TileType.BLOCKED;
        }

        return grid[row][col];
    }

    public boolean isBuildable(int row, int col) {
        return getTile(row, col) == TileType.BUILDABLE;
    }

    public List<Vector2> getWaypoints() {
        return waypoints;
    }

    public int getRows() {
        return grid.length;
    }

    public int getCols() {
        return grid[0].length;
    }

    public boolean canBuild(int row, int col) {
        return isBuildable(row, col) && !occupied[row][col];
    }

    public void markOccupied(int row, int col) {
        occupied[row][col] = true;
    }

    public int rowFromY(double y) {
        return (int) (y / TILE_SIZE);
    }

    public int colFromX(double x) {
        return (int) (x / TILE_SIZE);
    }

    public int[] findBlockContaining(int row, int col) {
        for (int[] block : buildableBlocks) {
            if (row >= block[0] && row < block[0] + blockSize
                    && col >= block[1] && col < block[1] + blockSize) {
                return block;
            }
        }
        return null;
    }

    public boolean isBlockFree(int[] block) {
        if (block == null)
            return false;
        for (int dr = 0; dr < blockSize; dr++) {
            for (int dc = 0; dc < blockSize; dc++) {
                if (occupied[block[0] + dr][block[1] + dc]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void markBlockOccupied(int[] block) {
        for (int dr = 0; dr < blockSize; dr++) {
            for (int dc = 0; dc < blockSize; dc++) {
                occupied[block[0] + dr][block[1] + dc] = true;
            }
        }
    }
}