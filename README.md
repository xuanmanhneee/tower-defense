# Tower Defense

Game thủ thành 2D viết bằng Java 17 và JavaFX, xây trên một engine nhỏ theo kiến trúc component-based giống Unity.

## Kiến trúc

- `GameObject` chứa `Transform` và danh sách `Behaviour`.
- `Behaviour` là component, có các hook `start`, `update`, `onDestroy`.
- Loại thực thể được quyết định bởi tập Behaviour gắn vào (địch, tháp, đạn đều là `GameObject`).
- `Prefab` tạo sẵn một tổ hợp Behaviour; `Scene.instantiate(prefab, position)` đưa vào game.
- `Scene` quản lý vòng đời, việc tạo và huỷ object được áp dụng cuối frame.
- Giao tiếp giữa các thành phần qua `getBehaviour(Class)` và `EventBus`.

```java
GameObject enemy = new GameObject();
enemy.addBehaviour(new Health(500));
enemy.addBehaviour(new PathFollower(50));
enemy.addBehaviour(new CircleCollider(12, CollisionLayer.ENEMY).detects(CollisionLayer.GOAL));
enemy.addBehaviour(new RewardOnDeath(10));
```

## Cấu trúc

| Package | Nội dung |
|---|---|
| `core` | `GameObject`, `Behaviour`, `Scene`, `Prefab`, `EventBus`, `ObjectPool` |
| `behaviour` | Component gameplay: máu, di chuyển, nhắm, bắn, sát thương |
| `collision` | Va chạm tròn theo layer/mask |
| `entity` | Chiến lược bắn, hiệu ứng trúng đạn |
| `prefab` | Địch, tháp, đạn, Goal |
| `map` | Lưới, đường đi, ô xây |
| `render`, `ui` | Vẽ Canvas, giao diện |

Chi tiết API: [docs/](docs/README.md)

## Chạy

Yêu cầu JDK 17+ và Maven.

```bash
mvn javafx:run
```
