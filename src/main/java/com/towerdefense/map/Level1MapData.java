package com.towerdefense.map;

public final class Level1MapData {

    private Level1MapData() {
    }

    public static final int ROWS = 24;
    public static final int COLS = 40;

    // Mỗi mảng int[] là {fromRow, fromCol, toRow, toCol} - đoạn thẳng của đường đi.
    public static final int[][] PATH_SEGMENTS = {
            { 8, 0, 8, 10 },
            { 8, 10, 15, 10 },
            { 15, 10, 15, 28 },
            { 15, 28, 5, 28 },
            { 5, 28, 5, COLS - 1 }
    };

    // Toạ độ góc trên-trái của mỗi cụm buildable (kích thước theo
    // GameConfig.TOWER_SIZE_IN_TILES).
    public static final int[][] BUILDABLE_BLOCKS = {
            { 5, 2 }, { 5, 6 }, { 11, 12 }, { 18, 14 },
            { 17, 22 }, { 10, 25 }, { 2, 30 }, { 8, 33 }
    };
}