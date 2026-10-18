# ui

Package `com.towerdefense.ui`. UI vẽ tay lên canvas riêng (`uiCanvas`), hit-test bằng toạ độ.

`App` chuyển Esc tới `GameScene` để pause/resume, đồng thời vẫn forward mọi phím nhấn, bao gồm F3, tới debug panel.

## UIScreen
```java
interface UIScreen {
    void update(double deltaTime);
    void render(GraphicsContext graphics);
    void handleClick(double x, double y);
}
```

## UIManager
| Member | Mô tả |
|---|---|
| `void setScreen(UIScreen)` | Đổi màn hình hiện tại. |
| `void update(double)`, `void render(GraphicsContext)`, `void handleClick(double, double)` | Chuyển tiếp tới màn hình hiện tại. |
| `void showTowerSelection(List<TowerOption>, Consumer<TowerOption> onSelected)` | Hiện `TowerSelectionScreen`. |

## Screens

`GameHudScreen` quan sát trạng thái pause của `GameScene`. Khi pause, nó vẽ overlay phủ canvas với `PAUSED` và `Press Esc to resume`; chỉ gameplay simulation dừng, còn render/HUD vẫn chạy.

| Class | Mô tả |
|---|---|
| `GameHudScreen` | HUD và overlay pause khi `GameScene.isPaused()` là true: phủ canvas với `PAUSED` và `Press Esc to resume`. |
| `TowerSelectionScreen(List<TowerOption>, Consumer<TowerOption>)` | Bảng chọn tháp; click một mục gọi `onSelected`. |

## TowerOption
`record TowerOption(String name, int cost)`

Lưu ý: `App` chưa chuyển click vào `UIManager`.
