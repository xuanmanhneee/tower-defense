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
import com.towerdefense.ui.MainMenuView;
import com.towerdefense.ui.PauseMenuView;
import com.towerdefense.ui.TowerOption;
import com.towerdefense.ui.TowerSelectionScreen;
import com.towerdefense.ui.UIManager;

import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    private static final int WIDTH = GameConfig.WINDOW_WIDTH;
    private static final int HEIGHT = GameConfig.WINDOW_HEIGHT;

    private long lastTime = 0;
    private boolean playing;
    private boolean paused;
    private Button pauseButton;
    private PauseMenuView pauseMenu;

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
                towerSelection::getSelectedTower,
                gameScene.getWaveSpawner()));

        DebugPanelScreen debugPanel = new DebugPanelScreen(DebugMetrics.getInstance());
        uiManager.addScreen(debugPanel);

        FpsOverlayScreen fpsOverlay = new FpsOverlayScreen();
        uiManager.addScreen(fpsOverlay);

        gameCanvas.setOnMouseClicked(event -> {
            if (!playing || paused) {
                return;
            }
            if (!uiManager.handleClick(event.getX(), event.getY())) {
                gameScene.tryPlaceTower(
                        event.getX(),
                        event.getY(),
                        towerSelection.getSelectedTower());
            }
        });
        gameCanvas.setOnMouseMoved(event ->
                uiManager.handleMouseMove(event.getX(), event.getY()));

        MapRenderer mapRenderer = new MapRenderer();
        GameRenderer gameRenderer = new GameRenderer(gameGraphics);

        pauseButton = new Button("Ⅱ  PAUSE");
        pauseButton.getStyleClass().add("pause-button");
        pauseButton.setVisible(false);
        pauseButton.setManaged(false);
        pauseButton.setOnAction(event -> setPaused(true));
        StackPane.setAlignment(pauseButton, Pos.TOP_RIGHT);
        StackPane.setMargin(pauseButton, new Insets(20));

        pauseMenu = new PauseMenuView(
                () -> setPaused(false),
                Platform::exit);

        MainMenuView mainMenu = new MainMenuView(
                this::startGame,
                fpsOverlay::setVisible,
                Platform::exit);

        StackPane root = new StackPane(
                gameCanvas,
                uiCanvas,
                pauseButton,
                mainMenu,
                pauseMenu);

        javafx.scene.Scene fxScene = new javafx.scene.Scene(root, WIDTH, HEIGHT);
        fxScene.getStylesheets().add(
                App.class.getResource("/ui/menu.css").toExternalForm());
        fxScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE && playing) {
                setPaused(!paused);
                return;
            }
            debugPanel.handleKeyPressed(event.getCode());
        });

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

                if (!playing) {
                    return;
                }

                if (!paused) {
                    gameScene.update(deltaTime);
                }

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

    private void startGame() {
        playing = true;
        paused = false;
        pauseButton.setManaged(true);
        pauseButton.setVisible(true);
    }

    private void setPaused(boolean paused) {
        if (!playing) {
            return;
        }

        this.paused = paused;
        if (paused) {
            pauseButton.setVisible(false);
            pauseButton.setManaged(false);
            pauseMenu.showMenu();
        } else {
            pauseMenu.hideMenu();
            pauseButton.setManaged(true);
            pauseButton.setVisible(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
