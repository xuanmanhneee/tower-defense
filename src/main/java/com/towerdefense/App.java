package com.towerdefense;

import com.towerdefense.config.GameConfig;
import com.towerdefense.debug.DebugMetrics;
import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.game.GameFlowController;
import com.towerdefense.game.GameState;
import com.towerdefense.render.GameRenderer;
import com.towerdefense.render.MapRenderer;
import com.towerdefense.scene.GameScene;
import com.towerdefense.ui.GameOverScreen;
import com.towerdefense.ui.GameHudScreen;
import com.towerdefense.ui.DebugPanelScreen;
import com.towerdefense.ui.FpsOverlayScreen;
import com.towerdefense.ui.MainMenuScreen;
import com.towerdefense.ui.PauseMenuScreen;
import com.towerdefense.ui.TowerOption;
import com.towerdefense.ui.TowerSelectionScreen;
import com.towerdefense.ui.UIManager;

import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

        GameFlowController flowController = new GameFlowController();
        UIManager uiManager = new UIManager();
        DebugPanelScreen debugPanel = new DebugPanelScreen(DebugMetrics.getInstance());
        FpsOverlayScreen fpsOverlay = new FpsOverlayScreen();

        MapRenderer mapRenderer = new MapRenderer();
        GameRenderer gameRenderer = new GameRenderer(gameGraphics);
        double[] mousePos = new double[]{-1, -1};

        class ScreenComposer {
            private GameScene composedScene;
            private GameFlowController.View composedView;
            private TowerSelectionScreen towerSelection;
            private GameHudScreen gameHud;

            void compose() {
                GameFlowController.View view = flowController.getView();
                GameScene scene = flowController.getCurrentGameScene().orElse(null);
                if (view == composedView && scene == composedScene) {
                    return;
                }

                if (view == GameFlowController.View.MAIN_MENU) {
                    composedScene = null;
                    towerSelection = null;
                    gameHud = null;
                    uiManager.setScreen(new MainMenuScreen(
                            () -> {
                                flowController.startNewGame();
                                compose();
                            },
                            Platform::exit));
                    composedView = view;
                    return;
                }

                if (scene == null) {
                    throw new IllegalStateException("Gameplay view requires an active game scene");
                }
                if (scene != composedScene) {
                    composedScene = scene;
                    towerSelection = new TowerSelectionScreen(
                            Arrays.stream(TowerType.values())
                                    .map(TowerOption::new)
                                    .toList(),
                            ignored -> {
                            });
                    gameHud = new GameHudScreen(
                            GameState.getInstance(),
                            towerSelection::getSelectedTower);
                }

                uiManager.setScreen(towerSelection);
                uiManager.addScreen(gameHud);
                if (view == GameFlowController.View.PAUSED) {
                    uiManager.addScreen(new PauseMenuScreen(
                            () -> {
                                flowController.resume();
                                compose();
                            },
                            () -> {
                                flowController.startNewGame();
                                compose();
                            },
                            () -> {
                                flowController.returnToMainMenu();
                                compose();
                            }));
                } else if (view == GameFlowController.View.GAME_OVER) {
                    uiManager.addScreen(new GameOverScreen(
                            scene::getSessionStats,
                            () -> {
                                flowController.startNewGame();
                                compose();
                            },
                            () -> {
                                flowController.returnToMainMenu();
                                compose();
                            }));
                }
                uiManager.addScreen(debugPanel);
                uiManager.addScreen(fpsOverlay);
                composedView = view;
            }

            TowerSelectionScreen getTowerSelection() {
                return towerSelection;
            }
        }

        ScreenComposer screenComposer = new ScreenComposer();
        screenComposer.compose();

        gameCanvas.setOnMouseClicked(event -> {
            if (!uiManager.handleClick(event.getX(), event.getY())) {
                if (flowController.getView() == GameFlowController.View.PLAYING) {
                    flowController.getCurrentGameScene().ifPresent(scene ->
                            scene.tryPlaceTower(
                                    event.getX(),
                                    event.getY(),
                                    screenComposer.getTowerSelection().getSelectedTower()));
                }
            }
        });

        Pane root = new Pane(
                gameCanvas,
                uiCanvas);

        javafx.scene.Scene fxScene = new javafx.scene.Scene(root, WIDTH, HEIGHT);
        fxScene.setOnMouseMoved(event -> {
            mousePos[0] = event.getX();
            mousePos[1] = event.getY();
        });
        fxScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                flowController.handleEscape();
                screenComposer.compose();
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

                GameFlowController.View viewBeforeUpdate = flowController.getView();
                flowController.update(deltaTime);
                if (viewBeforeUpdate != flowController.getView()) {
                    screenComposer.compose();
                }

                gameGraphics.clearRect(0, 0, WIDTH, HEIGHT);
                GameScene currentGame = flowController.getCurrentGameScene().orElse(null);
                if (currentGame == null) {
                    gameGraphics.setFill(Color.rgb(12, 17, 25));
                    gameGraphics.fillRect(0, 0, WIDTH, HEIGHT);
                } else {
                    mapRenderer.render(currentGame.getMap(), gameGraphics);
                    gameRenderer.render(currentGame.getScene().getGameObjects());
                    mapRenderer.renderHoverAndRange(
                            currentGame.getMap(),
                            gameGraphics,
                            mousePos[0],
                            mousePos[1],
                            screenComposer.getTowerSelection().getSelectedTower());
                }

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
