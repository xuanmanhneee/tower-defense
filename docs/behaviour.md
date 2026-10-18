# behaviour

Package `com.towerdefense.behaviour`. Tất cả extends `Behaviour`.

`onReset()` (khi object được tái sử dụng từ pool) đã cài cho: `Health` (đầy máu, giữ `onDeath`), `PathFollower` (về waypoint đầu, giữ danh sách), `FireControl` (cooldown 0), `Targeting` (xoá mục tiêu), `HomingMotion` (xoá mục tiêu/hướng), `AutoDestroy` (đếm lại), `LinearMotion`, `TravelLimit`, `SpeedModifiers`.

| Class | Constructor | API / hành vi |
|---|---|---|
| `Health` | `(double maxHealth)` | `getHealth()`, `getMaxHealth()`, `getRatio()`, `isDead()`, `damage(double)`, `setOnDeath(Runnable)`. Về 0 → `destroy()` + chạy `onDeath`. |
| `PathFollower` | `(double speed)` | `setWaypoints(List<Vector2>)`. Đi qua từng waypoint; nhân tốc độ với `SpeedModifiers`. |
| `SpeedModifiers` | `()` | `add(double multiplier, double duration)`, `getMultiplier()` (tối thiểu 0.4). |
| `RewardOnDeath` | `(int gold)` | Khi `onDestroy` và `Health.isDead()` → publish `DeathEvent`. |
| `ContactDamage` | `(double amount, boolean destroyOnHit)` | `setAmount(double)`. Implements `CollisionListener`: gây sát thương lên `Health` của đối tượng chạm. |
| `GoalReceiver` | `()` | Implements `CollisionListener`: huỷ địch chạm vào. |
| `Targeting` | `(double range)` | `getCurrentTarget()`. Chọn `PathFollower` gần nhất trong tầm mỗi frame. |
| `FireControl` | `(Targeting, FireStrategy, double interval)` | Bắn theo `interval` khi có mục tiêu. |
| `Launchable` | abstract | `launch(Vector2 direction, GameObject target)`. |
| `LinearMotion` | `(double speed)` | Extends `Launchable`. Bay thẳng theo hướng đã `launch`. |
| `HomingMotion` | `(double speed)` | `setTarget(GameObject)`. Bay theo mục tiêu; mục tiêu chết thì giữ hướng cuối. |
| `TravelLimit` | `(double maxDistance)` | Huỷ object khi đi quá quãng đường từ vị trí `start`. |
| `AutoDestroy` | `(double seconds)` | Huỷ sau thời gian. |
| `HitHandler` | `(HitEffect)` | Implements `CollisionListener`: áp `HitEffect` lên đối tượng chạm rồi huỷ chính nó. |
| `SlowOnHit` | `(double multiplier, double duration)` | Implements `CollisionListener`: thêm làm chậm vào `SpeedModifiers` của đối tượng chạm. |
