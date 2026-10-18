# map

Package `com.towerdefense.map`. Toạ độ ô theo `(row, col)`; kích thước ô `GameConfig.TILE_SIZE` (32 px).

## TileType
`enum TileType { PATH, BUILDABLE, BLOCKED }`

## GameMap
`GameMap(TileType[][] grid, List<Vector2> waypoints, int[][] buildableBlocks, int blockSize)`

| Member | Mô tả |
|---|---|
| `static final int TILE_SIZE` | |
| `TileType getTile(int row, int col)` | Ngoài lưới → `BLOCKED`. |
| `boolean isBuildable(int row, int col)` | Ô là `BUILDABLE`. |
| `boolean canBuild(int row, int col)` | Buildable và chưa bị chiếm. |
| `void markOccupied(int row, int col)` | |
| `List<Vector2> getWaypoints()` | Đường đi (pixel), bất biến. |
| `int getRows()`, `int getCols()` | |
| `int rowFromY(double y)`, `int colFromX(double x)` | Pixel → ô. |
| `int[] findBlockContaining(int row, int col)` | Khối xây `{row, col}` chứa ô, hoặc `null`. |
| `boolean isBlockFree(int[] block)` | Không ô nào trong khối bị chiếm. |
| `void markBlockOccupied(int[] block)` | |

## MapFactory
`static GameMap createLevel1Map()` — dựng lưới + waypoints từ `Level1MapData`.

## Level1MapData
| Hằng | Mô tả |
|---|---|
| `ROWS = 24`, `COLS = 40` | |
| `int[][] PATH_SEGMENTS` | Đoạn thẳng `{fromRow, fromCol, toRow, toCol}`. |
| `int[][] BUILDABLE_BLOCKS` | Góc trên-trái `{row, col}` của mỗi khối xây (kích thước `TOWER_SIZE_IN_TILES`). |
