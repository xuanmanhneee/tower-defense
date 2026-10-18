# ui

Package `com.towerdefense.ui`. UI is drawn on a dedicated JavaFX `Canvas`;
screens implement `UIScreen` and handle clicks by canvas coordinates.

`App` delegates application-level navigation to `GameFlowController`. The
application opens to the Main Menu and creates a `GameScene` only after Play.
Esc toggles pause/resume during play; F3 continues to reach the debug panel
during a game session.

## UIScreen

```java
interface UIScreen {
    void update(double deltaTime);
    void render(GraphicsContext graphics);
    boolean handleClick(double x, double y);
}
```

`handleClick` returns `true` when that screen consumed the click.

## UIManager

| Member | Description |
|---|---|
| `void setScreen(UIScreen)` | Clear the current stack and set its bottom screen. |
| `void addScreen(UIScreen)` | Add a screen above the existing screens. |
| `void update(double)`, `void render(GraphicsContext)` | Update/render in insertion order. |
| `boolean handleClick(double, double)` | Dispatch clicks from topmost screen to bottom; stop when one consumes the click. |
| `void showTowerSelection(List<TowerOption>, Consumer<TowerOption>)` | Add a `TowerSelectionScreen`. |

The in-game screen order is tower selection and HUD, an optional Pause/Game
Over modal, the debug panel, then the FPS overlay. Pause and Game Over modals
consume backdrop clicks so they cannot trigger tower selection or placement.

## Screens

| Class | Behavior |
|---|---|
| `MainMenuScreen(Runnable play, Runnable quit)` | Initial screen with “TOWER DEFENSE”, Play, and Quit. |
| `PauseMenuScreen(Runnable resume, Runnable restart, Runnable mainMenu)` | Dimmed modal with Resume, Restart, and Main Menu. Resume continues the same session; Restart creates a new one. |
| `GameOverScreen(Supplier<GameSessionStats>, Runnable restart, Runnable mainMenu)` | Terminal result screen showing enemies defeated and gold earned, with Restart and Main Menu. |
| `GameHudScreen(GameState, Supplier<TowerType>)` | Existing gold/lives and selected tower build/upgrade HUD; it does not own pause/game-over overlays. |
| `TowerSelectionScreen(List<TowerOption>, Consumer<TowerOption>)` | Tower selection panel; selecting an option invokes `onSelected`. A fresh screen selects the default tower. |
| `DebugPanelScreen` / `FpsOverlayScreen` | Existing in-session diagnostics, retained across play/pause/game-over view changes. |

Per-session results count enemy `DeathEvent` rewards. Starting gold is excluded,
spent gold does not reduce earned gold, and goal death does not count as an
enemy defeated. No wave statistics are shown because the game has no wave
system.

## TowerOption

`record TowerOption(String name, int cost)`
