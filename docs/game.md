# game, scene, event, config, App

## App
`class App extends Application` — tạo hai `Canvas` (game + UI), `AnimationTimer` làm game loop (`deltaTime` giới hạn 0.05s), và `GameFlowController` điều phối Main Menu, gameplay, pause, và game over. Ứng dụng mở vào Main Menu; Play mới tạo `GameScene`. Khi có session, thứ tự xử lý là controller update → `MapRenderer` → `GameRenderer` → `UIManager`. Esc chuyển qua controller để pause/resume; F3 tiếp tục được forward tới debug panel.

Pause giữ nguyên session và tower selection. Restart từ Pause hoặc Game Over tạo `GameScene` mới, reset gold/lives/statistics và chọn tower mặc định. Main Menu bỏ tham chiếu session hiện tại và dừng gameplay update.

## scene
### GameScene
`GameScene` giữ phase hiện tại (`PlayingState`, `PausedState`, hoặc `GameOverState`) và điều phối gameplay theo phase. Esc chuyển playing ↔ paused; game over là terminal. Chi tiết về vai trò State Pattern và các phase nằm trong [design-patterns.md](design-patterns.md).

| Member | Mô tả |
|---|---|
| `GameScene()` | Tạo map, enemy pools/spawner (1.6s), subscribe `DeathEvent` → cộng vàng và cập nhật `GameSessionStats`, tạo Goal. |
| `void update(double dt)` | Trong `PlayingState`: `CollisionSystem.update` → game-over guard → `Scene.update` → `EnemySpawner.update`; pause và game over không chạy gameplay simulation. |
| `boolean tryPlaceTower(double x, double y, TowerType type)` | Đặt tháp (qua `TowerFactory`) vào khối xây còn trống nếu đủ vàng và phase cho phép; `false` khi pause, game over, hoặc không đặt được. |
| `Scene getScene()`, `GameMap getMap()`, `GameSessionStats getSessionStats()` | Truy cập scene, map và thống kê chỉ đọc theo quy ước cho session hiện tại. |

Goal death phát `GameOverEvent`, không phải `DeathEvent`; do đó nó không cộng kill count hoặc gold earned.

### EnemySpawner
`EnemySpawner(Scene, Prefab enemy, List<Vector2> waypoints, double spawnInterval)` · `void update(double dt)` — định kỳ tạo địch tại waypoint đầu và gán đường đi.

## game
### GameState (Singleton)
| Member | Mô tả |
|---|---|
| `static GameState getInstance()` | Khởi tạo vàng 100, mạng 20. |
| `int getGold()`, `void addGold(int)`, `boolean spendGold(int)` | `spendGold` trả `false` nếu không đủ. |
| `int getLives()`, `void loseLife(int)`, `boolean isGameOver()` | |

### GameSessionStats
Mỗi `GameScene` sở hữu một `GameSessionStats`; bộ đếm bắt đầu từ zero và ghi số enemy bị hạ cùng tổng reward kiếm được trong session. Restart tạo stats mới. Gold earned độc lập với gold hiện còn trong `GameState`.

### GameFlowController
Điều phối các view cấp ứng dụng `MAIN_MENU`, `PLAYING`, `PAUSED`, `GAME_OVER`. Main Menu không thuộc `GamePhase`; phase trong `GameScene` vẫn chỉ điều khiển simulation gameplay.

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
