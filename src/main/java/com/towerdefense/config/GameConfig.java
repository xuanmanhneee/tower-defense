package com.towerdefense.config;

public final class GameConfig {

    private GameConfig() {
    }

    // Map
    public static final int TILE_SIZE = 32;

    // Window
    public static final int WINDOW_WIDTH = TILE_SIZE * 40;
    public static final int WINDOW_HEIGHT = TILE_SIZE * 24;

    // Gameplay mặc định
    public static final int STARTING_GOLD = 100;
    public static final int STARTING_LIVES = 20;

    public static final int TOWER_SIZE_IN_TILES = 2;
}