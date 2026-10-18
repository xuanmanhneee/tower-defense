# game, scene, event, config, App

## App
`class App extends Application` — tạo hai `Canvas` (game + UI), `AnimationTimer` làm game loop (`deltaTime` giới hạn 0.05s): `GameScene.update` → `MapRenderer` → `GameRenderer` → `UIManager`. App chuyển Esc tới `GameScene` để pause/resume và vẫn forward F3 tới debug panel.

## scene
### GameScene
`GameScene` giữ phase hiện tại (`PlayingState`, `PausedState`, hoặc `GameOverState`) và điều phối gameplay theo phase. Esc chuyển playing ↔ paused; game over là terminal. Chi tiết về vai trò State Pattern và các phase nằm trong [design-patterns.md](design-patterns.md).

| Member | Mô tả |
|---|---|
| `GameScene()` | Tạo map, `EnemySpawner` (Grunt qua `PooledPrefab`, 1.6s), subscribe `DeathEvent` → cộng vàng, tạo Goal. |
| `void update(double dt)` | Trong `PlayingState`: `CollisionSystem.update` → game-over guard → `Scene.update` → `EnemySpawner.update`; pause và game over không chạy gameplay simulation. |
| `boolean tryPlaceTower(double x, double y, TowerType type)` | Đặt tháp (qua `TowerFactory`) vào khối xây còn trống nếu đủ vàng và phase cho phép; `false` khi pause, game over, hoặc không đặt được. |
| `Scene getScene()`, `GameMap getMap()` | |

### EnemySpawner
`EnemySpawner(Scene, Prefab enemy, List<Vector2> waypoints, double spawnInterval)` · `void update(double dt)` — định kỳ tạo địch tại waypoint đầu và gán đường đi.

## game
### GameState (Singleton)
| Member | Mô tả |
|---|---|
| `static GameState getInstance()` | Khởi tạo vàng 100, mạng 20. |
| `int getGold()`, `void addGold(int)`, `boolean spendGold(int)` | `spendGold` trả `false` nếu không đủ. |
| `int getLives()`, `void loseLife(int)`, `boolean isGameOver()` | |

## event
| Event | Định nghĩa | Phát bởi |
|---|---|---|
| `DeathEvent` | `record (GameObject source, int reward)` | `RewardOnDeath` |
| `GameOverEvent` | `record ()` | `GameScene` (khi Goal hết máu) |

## config
`GameConfig` (final, hằng số):

| Hằng | Giá trị |
|---|---|
| `TILE_SIZE` | 32 |
| `WINDOW_WIDTH` / `WINDOW_HEIGHT` | `TILE_SIZE*40` / `TILE_SIZE*24` |
| `STARTING_GOLD` / `STARTING_LIVES` | 100 / 20 |
| `TOWER_SIZE_IN_TILES` | 2 |

## test
`TestCollisionMask` — kiểm thử `CircleCollider` chạy bằng `main()` (chưa dùng JUnit).
