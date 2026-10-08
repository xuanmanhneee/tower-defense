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
| `GruntEnemyPrefab` | 500 | 50 | 1 | 10 | 24 |
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
| `BasicTowerPrefab` | `SingleShotStrategy(BasicProjectilePrefab)` |
| `SpreadTowerPrefab` | `SpreadShotDecorator` (1 viên mỗi bên, 15°) |
| `SlowTowerPrefab` | `SingleShotStrategy(SlowProjectilePrefab)` |
| `ExplosiveTowerPrefab` | `SingleShotStrategy(ExplosiveProjectilePrefab)` |

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
| `SlowProjectilePrefab` | 450 | `SlowOnHit(DamageOnHit(6), 0.5, 2.0)` |
| `ExplosiveProjectilePrefab` | 380 | `ExplodeOnHit(DamageOnHit(15), 128, 8)` |
