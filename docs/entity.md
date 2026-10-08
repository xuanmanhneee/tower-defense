# entity

Package `com.towerdefense.entity`.

## tower.fire — Strategy / Decorator
```java
interface FireStrategy { void fire(Vector2 origin, Vector2 direction, GameObject target, World world); }
abstract class FireStrategyDecorator implements FireStrategy { protected FireStrategyDecorator(FireStrategy wrapped); }
```

| Class | Constructor | Mô tả |
|---|---|---|
| `SingleShotStrategy` | `(ProjectilePrefab)` | Tạo 1 đạn và `launch` về mục tiêu. |
| `SpreadShotDecorator` | `(FireStrategy main, FireStrategy sideShot, int extraShotsEachSide, double spreadAngleDegrees)` | Bắn `main` + các viên lệch góc hai bên bằng `sideShot`. |

## projectile.effect — Decorator
```java
interface HitEffect { void apply(GameObject target, GameObject projectile, World world); }
abstract class HitEffectDecorator implements HitEffect { protected HitEffectDecorator(HitEffect inner); }
```

| Class | Constructor | Mô tả |
|---|---|---|
| `DamageOnHit` | `(double damage)` | Gây sát thương lên `Health` của mục tiêu. |
| `ExplodeOnHit` | `(HitEffect inner, double radius, double damage)` | `inner` + sát thương lan trong bán kính lên mọi object có `Health` + tạo `ExplosionVisual`. |
| `SlowOnHit` | `(HitEffect inner, double factor, double duration)` | `inner` + thêm làm chậm vào `SpeedModifiers`. |
| `ExplosionVisual` | `(double maxRadius, double duration)` | `RenderBehaviour` (layer `OVERLAY`), tự huỷ khi hết thời gian. |

## tower
| Class | Mô tả |
|---|---|
| `RotateTowardTargetBehaviour(Targeting, double rotationSpeed)` | Xoay `Transform.rotation` (độ) về phía mục tiêu. |
| `TowerType` | `enum { BASIC, SPREAD, SLOW }` (chưa dùng). |
| `UpgradeType` | `enum { BASIC, SPREAD, SLOW }` (chưa dùng). |
| `TowerUpgrade` | Đang comment-out. |

## Chưa dùng / trùng lặp
`projectile.HomingProjectileMovement`, `projectile.StraightProjectileMovement` (trùng `HomingMotion`/`LinearMotion`), `enemy.SpriteRenderer` (rỗng).
