# ui

Package `com.towerdefense.ui`. UI vẽ tay lên canvas riêng (`uiCanvas`), hit-test bằng toạ độ.

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
| Class | Mô tả |
|---|---|
| `GameHudScreen` | HUD (hiện hiển thị chuỗi cố định). |
| `TowerSelectionScreen(List<TowerOption>, Consumer<TowerOption>)` | Bảng chọn tháp; click một mục gọi `onSelected`. |

## TowerOption
`record TowerOption(String name, int cost)`

Lưu ý: `App` chưa chuyển click vào `UIManager`.
