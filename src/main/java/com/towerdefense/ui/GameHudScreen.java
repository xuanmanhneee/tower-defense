package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.game.GameState;
import com.towerdefense.wave.WaveSpawner;
import com.towerdefense.wave.WaveState;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Supplier;

public class GameHudScreen implements UIScreen {

    private static final double WIDTH = 620;
    private static final double HEIGHT = 70;
    private static final double X = (GameConfig.WINDOW_WIDTH - WIDTH) / 2.0;
    private static final double Y = 18;

    private final GameState gameState;
    private final Supplier<TowerType> selectedTower;
    private final WaveSpawner waveSpawner;

    public GameHudScreen(
            GameState gameState,
            Supplier<TowerType> selectedTower,
            WaveSpawner waveSpawner) {
        this.gameState = gameState;
        this.selectedTower = selectedTower;
        this.waveSpawner = waveSpawner;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {
        drawPanel(graphics);
        drawTitle(graphics);
        drawDivider(graphics, X + 174);
        drawMetric(graphics, X + 194, "GOLD", "$" + gameState.getGold(), "#f2c96d");
        drawDivider(graphics, X + 290);
        drawMetric(graphics, X + 310, "BASE", String.valueOf(gameState.getLives()), "#ef7774");
        drawDivider(graphics, X + 392);
        drawWaveStatus(graphics);

        if (gameState.isGameOver()) {
            drawGameOver(graphics);
        }
    }

    private void drawPanel(GraphicsContext graphics) {
        graphics.setFill(Color.rgb(5, 15, 12, 0.95));
        graphics.fillRoundRect(X, Y, WIDTH, HEIGHT, 18, 18);
        graphics.setStroke(Color.web("#315c47"));
        graphics.setLineWidth(1);
        graphics.strokeRoundRect(X + 0.5, Y + 0.5, WIDTH - 1, HEIGHT - 1, 18, 18);
        graphics.setFill(Color.web("#70dda0"));
        graphics.fillRoundRect(X, Y + 16, 4, 38, 4, 4);
    }

    private void drawTitle(GraphicsContext graphics) {
        graphics.setFill(Color.web("#effbf3"));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        graphics.fillText("TOWER DEFENSE", X + 20, Y + 30);

        TowerType tower = selectedTower.get();
        graphics.setFill(Color.web("#789286"));
        graphics.setFont(Font.font("Segoe UI", 9));
        graphics.fillText(tower.getDisplayName() + " selected  •  $" + tower.getCost(), X + 20, Y + 49);
    }

    private void drawMetric(
            GraphicsContext graphics,
            double x,
            String label,
            String value,
            String accent) {
        graphics.setFill(Color.web("#718a7d"));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 8));
        graphics.fillText(label, x, Y + 24);
        graphics.setFill(Color.web(accent));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        graphics.fillText(value, x, Y + 47);
    }

    private void drawWaveStatus(GraphicsContext graphics) {
        String wave = waveSpawner.getState() == WaveState.COMPLETED
                ? "CLEARED"
                : "WAVE " + waveSpawner.getCurrentWaveNumber() + "/" + waveSpawner.getWaveCount();

        graphics.setFill(Color.web("#173c2b"));
        graphics.fillRoundRect(X + 412, Y + 14, 76, 25, 9, 9);
        graphics.setFill(Color.web("#9ef0bd"));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 9));
        graphics.fillText(wave, X + 425, Y + 31);

        String status;
        if (waveSpawner.getState() == WaveState.BETWEEN_WAVES) {
            status = String.format("Next in %.1fs", waveSpawner.getTimeUntilNextWave());
        } else if (waveSpawner.getState() == WaveState.COMPLETED) {
            status = "All waves cleared";
        } else {
            status = waveSpawner.getActiveEnemyCount() + " enemies active";
        }

        graphics.setFill(Color.web("#6ddc9c"));
        graphics.fillOval(X + 505, Y + 23, 7, 7);
        graphics.setFill(Color.web("#d8e9df"));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        graphics.fillText(status, X + 520, Y + 31);
    }

    private void drawDivider(GraphicsContext graphics, double x) {
        graphics.setStroke(Color.web("#20372d"));
        graphics.setLineWidth(1);
        graphics.strokeLine(x, Y + 14, x, Y + HEIGHT - 14);
    }

    private void drawGameOver(GraphicsContext graphics) {
        graphics.setFill(Color.rgb(2, 8, 6, 0.78));
        graphics.fillRect(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        graphics.setFill(Color.WHITE);
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 42));
        graphics.fillText("GAME OVER",
                GameConfig.WINDOW_WIDTH / 2.0 - 125,
                GameConfig.WINDOW_HEIGHT / 2.0);
    }

    @Override
    public boolean handleClick(double x, double y) {
        return gameState.isGameOver();
    }
}
