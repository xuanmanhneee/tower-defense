package com.towerdefense;

import com.towerdefense.config.GameConfig;
import com.towerdefense.debug.DebugMetrics;
import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.game.GameState;
import com.towerdefense.render.GameRenderer;
import com.towerdefense.render.MapRenderer;
import com.towerdefense.scene.GameScene;
import com.towerdefense.ui.GameHudScreen;
import com.towerdefense.ui.DebugPanelScreen;
import com.towerdefense.ui.FpsOverlayScreen;
import com.towerdefense.ui.TowerOption;
import com.towerdefense.ui.TowerSelectionScreen;
import com.towerdefense.ui.UIManager;

import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

    private static final int WIDTH = GameConfig.WINDOW_WIDTH;
    private static final int HEIGHT = GameConfig.WINDOW_HEIGHT;

    private long lastTime = 0;

    @Override
    public void start(Stage stage) {

        Canvas gameCanvas = new Canvas(WIDTH, HEIGHT);
        Canvas uiCanvas = new Canvas(WIDTH, HEIGHT);

        uiCanvas.setMouseTransparent(true);

        GraphicsContext gameGraphics = gameCanvas.getGraphicsContext2D();
        GraphicsContext uiGraphics = uiCanvas.getGraphicsContext2D();

        GameScene gameScene = new GameScene();
        UIManager uiManager = new UIManager();

        TowerSelectionScreen towerSelection = new TowerSelectionScreen(
                Arrays.stream(TowerType.values())
                        .map(TowerOption::new)
                        .toList(),
                ignored -> {
                });

        uiManager.addScreen(towerSelection);
        uiManager.addScreen(new GameHudScreen(
                GameState.getInstance(),
                towerSelection::getSelectedTower));

        DebugPanelScreen debugPanel = new DebugPanelScreen(DebugMetrics.getInstance());
        uiManager.addScreen(debugPanel);
        uiManager.addScreen(new FpsOverlayScreen());

        gameCanvas.setOnMouseClicked(event -> {
            if (!uiManager.handleClick(event.getX(), event.getY())) {
                gameScene.tryPlaceTower(
                        event.getX(),
                        event.getY(),
                        towerSelection.getSelectedTower());
            }
        });

        MapRenderer mapRenderer = new MapRenderer();
        GameRenderer gameRenderer = new GameRenderer(gameGraphics);

        Pane root = new Pane(
                gameCanvas,
                uiCanvas);

        javafx.scene.Scene fxScene = new javafx.scene.Scene(root, WIDTH, HEIGHT);
        fxScene.setOnKeyPressed(event -> debugPanel.handleKeyPressed(event.getCode()));

        stage.setTitle("Tower Defense");
        stage.setResizable(false);
        stage.setScene(fxScene);
        stage.show();

        AnimationTimer gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double frameDeltaTime = (now - lastTime) / 1_000_000_000.0;
                double deltaTime = Math.min(frameDeltaTime, 0.05);

                lastTime = now;

                gameScene.update(deltaTime);

                gameGraphics.clearRect(0, 0, WIDTH, HEIGHT);

                mapRenderer.render(gameScene.getMap(), gameGraphics);

                gameRenderer.render(gameScene.getScene().getGameObjects());


                // UI
                uiManager.update(frameDeltaTime);

                uiGraphics.clearRect(0, 0, WIDTH, HEIGHT);

                uiManager.render(uiGraphics);
            }
        };

        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
