# prefab

Package `com.towerdefense.prefab`. Mỗi prefab implements `Prefab` và trả về `GameObject` đã gắn đủ Behaviour.

## Goal
| Class | Mô tả |
|---|---|
| `GoalPrefab` | Đích. `Health(20)`, collider `GOAL` detects `ENEMY`, `GoalReceiver`, `DiscRenderer`, `HealthBarRenderer`. |

## enemy
Behaviour chung: `DiscRenderer(ENEMY)`, `Health`, `HealthBarRenderer`, `CircleCollider(ENEMY).detects(GOAL)`, `ContactDamage(goalDamage, true)`, `PathFollower`, `RewardOnDeath`, `SpeedModifiers`.

| Class | HP | Speed | Goal damage | Reward | Size |
|---|---|---|---|---|---|
| `GruntEnemyPrefab` | 60 | 55 | 1 | 10 | 24 |
| `ScoutEnemyPrefab` | 40 | 160 | 1 | 8 | 18 |
| `TankEnemyPrefab` | 400 | 45 | 5 | 30 | 30 |

Sau `instantiate`, cần `getBehaviour(PathFollower.class).setWaypoints(...)` (do `EnemySpawner` làm).

## tower — Template Method
`abstract class TowerPrefab implements Prefab`

| Hook | Mặc định |
|---|---|
| `double radius()` | 16 |
| `double range()` | 150 |
| `double fireInterval()` | 1.0 |
| `Color color()` | `CRIMSON` |
| `abstract FireStrategy createFireStrategy()` | — |

`instantiate()` (final): `DiscRenderer(TOWER)` + `Targeting(range)` + `FireControl`.

| Class | FireStrategy |
|---|---|
| `BasicTowerPrefab` | `SingleShotStrategy(ProjectilePools.BASIC)` |
| `SpreadTowerPrefab` | `SpreadShotDecorator` (`SPREAD_MAIN` + `SPREAD_SIDE`, 1 viên mỗi bên, 12°) |
| `SlowTowerPrefab` | `SingleShotStrategy(ProjectilePools.SLOW)` |
| `ExplosiveTowerPrefab` | `SingleShotStrategy(ProjectilePools.EXPLOSIVE)` |

## projectile — Template Method
`abstract class ProjectilePrefab implements Prefab`

| Hook | Mặc định |
|---|---|
| `double radius()` | 4 |
| `double maxRange()` | 180 |
| `abstract Color color()` | — |
| `abstract double speed()` | — |
| `abstract HitEffect hitEffect()` | — |

`instantiate()` (final): `DiscRenderer(PROJECTILE)`, `CircleCollider(PROJECTILE).detects(ENEMY)`, `LinearMotion`, `TravelLimit`, `HitHandler`.

| Class | Speed | HitEffect |
|---|---|---|
| `BasicProjectilePrefab(double damage = 10)` | 500 | `DamageOnHit` |
| `SlowProjectilePrefab` | 550 | `SlowOnHit(DamageOnHit(10), 0.55, 2.5)` |
| `ExplosiveProjectilePrefab` | 480 | `ExplodeOnHit(DamageOnHit(28), 96, 14, pooled ExplosionPrefab)` |

### ProjectilePools
`final class ProjectilePools` — các `PooledPrefab` đạn dùng chung (tạo sẵn 16 object mỗi pool). Mọi tháp cùng loại dùng chung một pool.

| Hằng | Nguồn |
|---|---|
| `BASIC` | `BasicProjectilePrefab(20)` (tháp Basic) |
| `SPREAD_MAIN` | `BasicProjectilePrefab(14)` (đạn chính của Spread) |
| `SPREAD_SIDE` | `BasicProjectilePrefab(7)` (đạn phụ của Spread) |
| `SLOW` | `SlowProjectilePrefab` |
| `EXPLOSIVE` | `ExplosiveProjectilePrefab` |
