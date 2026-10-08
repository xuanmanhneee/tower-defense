package com.towerdefense;

import com.towerdefense.config.GameConfig;
import com.towerdefense.render.GameRenderer;
import com.towerdefense.render.MapRenderer;
import com.towerdefense.scene.GameScene;
import com.towerdefense.ui.GameHudScreen;
import com.towerdefense.ui.UIManager;

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

        gameCanvas.setOnMouseClicked(event -> {
            gameScene.handleClick(event.getX(), event.getY());
        });

        MapRenderer mapRenderer = new MapRenderer();
        GameRenderer gameRenderer = new GameRenderer(gameGraphics);

        UIManager uiManager = new UIManager();

        uiManager.setScreen(
                new GameHudScreen());

        Pane root = new Pane(
                gameCanvas,
                uiCanvas);

        javafx.scene.Scene fxScene = new javafx.scene.Scene(root, WIDTH, HEIGHT);

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

                double deltaTime = Math.min((now - lastTime) / 1_000_000_000.0, 0.05);

                lastTime = now;

                gameScene.update(deltaTime);

                gameGraphics.clearRect(0, 0, WIDTH, HEIGHT);

                mapRenderer.render(gameScene.getMap(), gameGraphics);

                gameRenderer.render(gameScene.getScene().getGameObjects());


                // UI
                uiManager.update(deltaTime);

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