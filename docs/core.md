# core

Package `com.towerdefense.core`. Nền Entity–Component, không phụ thuộc JavaFX.

## Behaviour
`abstract class Behaviour` — thành phần gắn vào `GameObject`.

| Member | Mô tả |
|---|---|
| `protected GameObject gameObject` | Chủ sở hữu. |
| `final GameObject getGameObject()` | |
| `void start()` | Gọi một lần khi object vào Scene (sau khi mọi Behaviour đã gắn). |
| `void update(double deltaTime)` | Mỗi frame (giây). |
| `void onDestroy()` | Sau khi object bị gỡ khỏi Scene. |
| `void onReset()` | Hook cho pool; `GameObject.reset` gọi để đưa trạng thái động về ban đầu. |

## GameObject
| Member | Mô tả |
|---|---|
| `Transform getTransform()` | |
| `Scene getScene()` | `null` trước khi vào Scene. |
| `<T extends Behaviour> T addBehaviour(T)` | Trả lại chính behaviour. Thứ tự gắn = thứ tự update. |
| `<T extends Behaviour> T getBehaviour(Class<T>)` | Phần tử đầu tiên `isInstance`; `null` nếu không có. |
| `List<Behaviour> getBehaviours()` | View chỉ-đọc. |
| `void destroy()` | Đánh dấu chết; Scene gỡ cuối frame. |
| `boolean isAlive()` | |
| `void reset(Vector2 position)` | `alive=true`, đặt vị trí, rotation = 0, gọi `onReset()` của mọi Behaviour (dùng cho pool). |
| `start()`, `update(dt)`, `notifyDestroyed()` | Dành cho `Scene`. |

## Scene
`class Scene implements World`

| Member | Mô tả |
|---|---|
| `GameObject instantiate(Prefab)` | Tạo, vào Scene cuối frame. |
| `GameObject instantiate(Prefab, Vector2)` | Như trên, đặt vị trí trước `start()`. |
| `List<GameObject> findObjectsWithBehaviour(Class<? extends Behaviour>)` | List mới các object sống có behaviour đó. |
| `List<GameObject> getGameObjects()` | View chỉ-đọc (gồm object vừa `destroy`). |
| `EventBus getEventBus()` | |
| `void update(double dt)` | update → dọn object chết (`onDestroy`) → thêm object chờ (`start`). |

## World, TargetProvider, ObjectSpawner
```java
interface TargetProvider { List<GameObject> findObjectsWithBehaviour(Class<? extends Behaviour> type); }
interface ObjectSpawner  { GameObject instantiate(Prefab prefab, Vector2 position); }
interface World extends TargetProvider, ObjectSpawner {}
```

## Prefab
```java
@FunctionalInterface interface Prefab { GameObject instantiate(); }
```

## EventBus, GameEvent
```java
interface GameEvent {}
class EventBus {
    <T extends GameEvent> void subscribe(Class<T> type, Consumer<T> listener);
    void publish(GameEvent event);   // đồng bộ, khớp đúng lớp của event
}
```

## Transform
| Member | Mô tả |
|---|---|
| `Transform()`, `Transform(Vector2)` | |
| `Vector2 getPosition()` / `setPosition(Vector2)` | |
| `double getRotation()` / `setRotation(double)` | Độ. |
| `void translate(Vector2 offset)` | |

## Vector2
`record Vector2(double x, double y)` — bất biến.

| Member | Mô tả |
|---|---|
| `static final Vector2 ZERO` | |
| `add(Vector2)`, `subtract(Vector2)`, `multiply(double)` | Trả Vector2 mới. |
| `length()`, `lengthSquared()`, `distanceTo(Vector2)` | |
| `normalized()` | `ZERO` nếu độ dài ≈ 0. |

## ObjectPool
Tái sử dụng `GameObject` thay vì tạo mới.

| Member | Mô tả |
|---|---|
| `ObjectPool(Supplier<GameObject> factory)` | Không giới hạn số object rảnh. |
| `ObjectPool(Supplier<GameObject> factory, int maxSize)` | `maxSize > 0`; trả về khi đã đầy thì object bị bỏ. |
| `void prewarm(int count)` | Tạo sẵn object rảnh, không vượt `maxSize`. |
| `GameObject obtain(Vector2 position)` | Lấy object rảnh (gọi `reset`) hoặc tạo mới. Không tự vào Scene. |
| `int getCreatedCount()` | Tổng số object tạo bằng factory. |
| `int getReusedCount()` | Số lần `obtain` lấy được object cũ. |
| `int getFreeCount()` | Số object đang rảnh. |
| `int getActiveCount()` | Số object đang được dùng. |

Object tạo bởi pool tự trả về pool khi `onDestroy` (Behaviour nội bộ `ReturnToPool`, luôn là Behaviour cuối). Trả hai lần được bỏ qua.

## PooledPrefab
`class PooledPrefab implements Prefab` — Decorator đưa `ObjectPool` vào mọi nơi nhận `Prefab`.

| Member | Mô tả |
|---|---|
| `PooledPrefab(Prefab source)` | |
| `PooledPrefab(Prefab source, int prewarm)` | Tạo sẵn `prewarm` object. |
| `GameObject instantiate()` | `pool.obtain(Vector2.ZERO)`; `Scene.instantiate(prefab, pos)` đặt vị trí sau đó. |
| `ObjectPool getPool()` | Xem bộ đếm. |

Giữ một instance dùng chung; tạo mới mỗi lần thì pool vô tác dụng.

## Quy ước tái sử dụng
```
obtain → reset(pos) → Scene: setScene, start() → update… → destroy() → onDestroy → về pool
```
- `GameObject.reset(pos)`: `alive = true`, đặt vị trí, `rotation = 0`, rồi gọi `onReset()` của mọi Behaviour.
- `onReset()` đưa trạng thái động về ban đầu; cấu hình (vd `Health.onDeath`, danh sách waypoint) được giữ.
- `start()` chạy lại mỗi lần object vào Scene; không giả định chỉ chạy một lần.
