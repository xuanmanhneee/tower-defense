# Design Patterns trong game

Tài liệu này giải thích các design pattern đang dùng trong game và cách chúng tương ứng với code.

## State Pattern: trạng thái game

Pause/resume và game over sử dụng State Pattern. Thay vì rải điều kiện kiểm tra trạng thái trong gameplay, `GameScene` ủy quyền hành vi phụ thuộc phase cho `GamePhase` hiện tại.

### Các vai trò

| Vai trò trong State Pattern | Thành phần trong game | Trách nhiệm |
|---|---|---|
| Context | `GameScene` | Sở hữu phase hiện tại, điều phối update và đổi phase khi pause hoặc game over xảy ra. |
| State contract | `GamePhase` | Định nghĩa cách update, chuyển pause, cho phép đặt tháp và truy vấn phase. |
| Concrete state | `PlayingState` | Chạy gameplay simulation và cho phép đặt tháp. Nhấn Esc chuyển sang `PausedState`. |
| Concrete state | `PausedState` | Không update gameplay và từ chối đặt tháp. Nhấn Esc chuyển lại `PlayingState`. |
| Concrete state | `GameOverState` | Không update gameplay, từ chối đặt tháp và giữ terminal khi nhấn Esc. |

Các phase và contract nằm trong package `com.towerdefense.scene`.

### Chuyển phase

| Phase hiện tại | Sự kiện | Phase tiếp theo |
|---|---|---|
| `PlayingState` | Esc | `PausedState` |
| `PausedState` | Esc | `PlayingState` |
| `PlayingState` or `PausedState` | `GameOverEvent` | `GameOverState` |
| `GameOverState` | Esc | `GameOverState` |

`GameOverEvent` được phát khi goal chết sẽ thay thế phase hiện tại, kể cả khi game đang pause. Phạm vi hiện tại không có chuyển trạng thái restart.

### Gameplay update và đặt tháp

`PlayingState.update` gọi `GameScene.updateGameplay`. Thứ tự update khi đang chơi:

1. Xử lý collision detection.
2. Kiểm tra collision có gây game over không; nếu có, dừng gameplay update trong frame này.
3. Update scene, bao gồm di chuyển enemy.
4. Update enemy spawner.

`PausedState` và `GameOverState` không gọi gameplay update. Vì vậy collision processing, scene simulation và spawn timer dừng khi pause hoặc game over. Animation/render loop, HUD, FPS overlay và debug panel vẫn tiếp tục chạy.

Chỉ `PlayingState` cho phép đặt hoặc nâng cấp tháp. `GameScene.tryPlaceTower` từ chối yêu cầu trước khi tìm block, trừ vàng, đổi trạng thái occupancy hoặc tạo/hủy tower nếu phase hiện tại không cho phép.

### Input và UI

`App` chuyển phím Esc tới `GameScene.togglePause()` và vẫn gửi các phím nhấn tới debug panel, bao gồm F3. `GameHudScreen` theo dõi `GameScene.isPaused()` qua `BooleanSupplier` và vẽ pause overlay trên HUD hiện có. Overlay pause được vẽ trước game-over overlay; `GameOverState.isPaused()` trả về `false`, nên game over không bị coi là pause.

### Phân biệt với `GameState`

`GamePhase` được đặt tên riêng để phân biệt với `GameState`. `GameState` vẫn là singleton hiện có, dùng lưu gold và lives; nó không phải State Pattern.

### Source và test

- `src/main/java/com/towerdefense/scene/GamePhase.java`
- `src/main/java/com/towerdefense/scene/PlayingState.java`
- `src/main/java/com/towerdefense/scene/PausedState.java`
- `src/main/java/com/towerdefense/scene/GameOverState.java`
- `src/main/java/com/towerdefense/scene/GameScene.java`
- `src/test/java/com/towerdefense/scene/GameSceneStateTest.java`

Test bao phủ chuyển phase, game over terminal, đóng băng simulation và collision khi pause, hành vi spawn, resume, và từ chối build/upgrade tháp khi pause.

## Pattern khác và thành phần liên quan

- **Singleton — `GameState`:** cung cấp trạng thái gold và lives dùng chung. Xem [game.md](game.md).
- **Factory — `MapFactory` và `TowerFactory`:** tập trung việc tạo map và tower. Xem [map.md](map.md) và [game.md](game.md).
- **Object Pool — `ObjectPool`:** tái sử dụng game object, bao gồm enemy được spawn. Xem [core.md](core.md).
