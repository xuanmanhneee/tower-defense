# render

Package `com.towerdefense.render`. Vẽ bằng JavaFX `GraphicsContext`.

## RenderLayer
`enum RenderLayer { RANGE, TOWER, ENEMY, PROJECTILE, OVERLAY }` — `GameRenderer` hiện chưa sắp xếp theo layer.

## RenderBehaviour
`abstract class RenderBehaviour extends Behaviour`

| Member | Mô tả |
|---|---|
| `protected RenderBehaviour(RenderLayer)` | |
| `RenderLayer getLayer()` | |
| `abstract void render(GraphicsContext)` | |

## Renderers
| Class | Constructor | Mô tả |
|---|---|---|
| `DiscRenderer` | `(RenderLayer, Color, double radius)` / `(…, boolean solid)` | Vẽ hình tròn tại Transform; `setColor(Color)`. |
| `HealthBarRenderer` | `(double width, double height, double offsetY)` | Thanh máu từ `Health`; ẩn khi chết. |
| `TowerLevelRenderer` | — | Đang comment-out. |

## GameRenderer
| Member | Mô tả |
|---|---|
| `GameRenderer(GraphicsContext)` | |
| `void render(Collection<GameObject>)` | Gọi `render` của mọi `RenderBehaviour` trên object còn sống. |

## MapRenderer
`void render(GameMap, GraphicsContext)` — tô màu từng ô theo `TileType`.

## TowerVisuals
`static Color colorFor(TowerType)` (chưa dùng).
