# collision

Package `com.towerdefense.collision`. Va chạm hình tròn, lọc theo layer/mask.

## CollisionLayer
`enum CollisionLayer { ENEMY, PROJECTILE, GOAL }`

## CircleCollider
`class CircleCollider extends Behaviour`

| Member | Mô tả |
|---|---|
| `CircleCollider(double radius, CollisionLayer layer)` | `radius > 0`. |
| `CircleCollider detects(CollisionLayer... layers)` | Thêm layer mà collider này quan tâm. Trả `this`. |
| `double getRadius()` | |
| `Vector2 getCenter()` | Vị trí Transform. |
| `boolean interactsWith(CircleCollider other)` | `other.layer` có trong mask. Không đối xứng. |
| `boolean overlaps(CircleCollider other)` | Hai hình tròn giao nhau. |

## CollisionListener
```java
interface CollisionListener { void onCollision(GameObject other); }
```
Cài đặt trong Behaviour để nhận va chạm.

## CollisionSystem
| Member | Mô tả |
|---|---|
| `void update(List<GameObject> objects)` | Duyệt mọi cặp collider; nếu mask khớp và chồng lên nhau thì gọi `onCollision` trên các `CollisionListener` của bên "quan tâm". Gọi trước `Scene.update` mỗi frame. |

Ví dụ: `new CircleCollider(4, PROJECTILE).detects(ENEMY)` — đạn chủ động nhận va chạm với địch.
