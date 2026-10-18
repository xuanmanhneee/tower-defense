# Tower Defense – Tài liệu API

Java 17 · JavaFX 17 · Maven. Kiến trúc Entity–Component: `GameObject` chứa các `Behaviour`.
Chạy: `mvn javafx:run`. Gốc package: `com.towerdefense`.

| Tài liệu | Package | Nội dung |
|---|---|---|
| [core.md](core.md) | `core` | GameObject, Behaviour, Scene, EventBus, ObjectPool, Vector2 |
| [behaviour.md](behaviour.md) | `behaviour` | Các Behaviour gameplay dùng chung |
| [collision.md](collision.md) | `collision` | Va chạm vòng tròn theo layer/mask |
| [entity.md](entity.md) | `entity` | Chiến lược bắn, hiệu ứng trúng đạn, tháp |
| [prefab.md](prefab.md) | `prefab` | Công thức tạo địch, tháp, đạn, goal |
| [map.md](map.md) | `map` | Lưới bản đồ, đường đi, ô xây |
| [render.md](render.md) | `render` | Vẽ Canvas |
| [ui.md](ui.md) | `ui` | Màn hình UI |
| [game.md](game.md) | `scene`, `game`, `event`, `config`, `App` | Điều phối game, trạng thái, sự kiện, hằng số |

Tài liệu liên quan: [../PLAN.md](../PLAN.md) (kế hoạch), [../CORE_API.md](../CORE_API.md) (phân tích core).
