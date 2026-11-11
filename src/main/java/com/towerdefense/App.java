package com.towerdefense;

import com.towerdefense.audio.AudioManager;
import com.towerdefense.audio.AudioResources;
import com.towerdefense.config.GameConfig;
import com.towerdefense.debug.DebugMetrics;
import com.towerdefense.entity.tower.TowerType;
import com.towerdefense.event.TowerFiredEvent;
import com.towerdefense.event.TowerUpgradedEvent;
import com.towerdefense.event.WavesClearedEvent;
import com.towerdefense.event.GameOverEvent;
import com.towerdefense.game.GameState;
import com.towerdefense.render.GameRenderer;
import com.towerdefense.render.MapRenderer;
import com.towerdefense.scene.GameScene;
import com.towerdefense.ui.GameHudScreen;
import com.towerdefense.ui.GameResultView;
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
    private GameScene gameScene;
    private UIManager uiManager;
    private TowerSelectionScreen towerSelection;
    private Button pauseButton;
    private PauseMenuView pauseMenu;
    private MainMenuView mainMenu;
    private GameResultView gameResult;

    @Override
    public void start(Stage stage) {

        Canvas gameCanvas = new Canvas(WIDTH, HEIGHT);
        Canvas uiCanvas = new Canvas(WIDTH, HEIGHT);

        uiCanvas.setMouseTransparent(true);

        GraphicsContext gameGraphics = gameCanvas.getGraphicsContext2D();
        GraphicsContext uiGraphics = uiCanvas.getGraphicsContext2D();

        DebugPanelScreen debugPanel = new DebugPanelScreen(DebugMetrics.getInstance());
        FpsOverlayScreen fpsOverlay = new FpsOverlayScreen();
        resetGame(debugPanel, fpsOverlay);

        gameCanvas.setOnMouseClicked(event -> {
            if (!playing || gameScene.isPaused()) {
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

        mainMenu = new MainMenuView(
                this::startGame,
                fpsOverlay::setVisible,
                Platform::exit);

        gameResult = new GameResultView(
                () -> {
                    resetGame(debugPanel, fpsOverlay);
                    gameResult.hideResult();
                    startGame();
                },
                () -> {
                    playing = false;
                    resetGame(debugPanel, fpsOverlay);
                    gameResult.hideResult();
                    mainMenu.showMenu();
                    AudioManager.getInstance().playMusic("/audios/menu.mp3");
                });

        StackPane root = new StackPane(
                gameCanvas,
                uiCanvas,
                pauseButton,
                mainMenu,
                pauseMenu,
                gameResult);

        javafx.scene.Scene fxScene = new javafx.scene.Scene(root, WIDTH, HEIGHT);
        fxScene.getStylesheets().add(
                App.class.getResource("/ui/menu.css").toExternalForm());
        fxScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE && playing) {
                setPaused(!gameScene.isPaused());
                return;
            }
            debugPanel.handleKeyPressed(event.getCode());
        });

        stage.setTitle("Tower Defense");
        stage.setResizable(false);
        stage.setScene(fxScene);
        stage.show();
        AudioManager.getInstance().playMusic("/audios/menu.mp3");

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

    private void startGame() {
        playing = true;
        setPaused(false);
        AudioManager.getInstance().playPlaylist(
                "/audios/bgm_1.mp3",
                "/audios/bgm_2.mp3");
        pauseButton.setManaged(true);
        pauseButton.setVisible(true);
    }

    @Override
    public void stop() {
        AudioManager.getInstance().stopAll();
    }

    private void resetGame(DebugPanelScreen debugPanel, FpsOverlayScreen fpsOverlay) {
        gameScene = new GameScene();
        connectAudio(gameScene);
        uiManager = new UIManager();
        towerSelection = new TowerSelectionScreen(
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
        uiManager.addScreen(debugPanel);
        uiManager.addScreen(fpsOverlay);
    }

    private void connectAudio(GameScene gameScene) {
        AudioManager audio = AudioManager.getInstance();
        gameScene.getScene().getEventBus().subscribe(
                TowerFiredEvent.class,
                event -> audio.playEffect(switch (event.towerType()) {
                    case BASIC -> "/audios/shoot.mp3";
                    case SPREAD -> "/audios/spread.mp3";
                    case SLOW -> "/audios/slow.mp3";
                    case EXPLOSIVE -> "/audios/explode.mp3";
                }));
        gameScene.getScene().getEventBus().subscribe(
                TowerUpgradedEvent.class,
                event -> audio.playEffect("/audios/upgrate_tower.mp3"));
        gameScene.getScene().getEventBus().subscribe(
                WavesClearedEvent.class,
                event -> showGameResult(true));
        gameScene.getScene().getEventBus().subscribe(
                GameOverEvent.class,
                event -> showGameResult(false));
    }

    private void showGameResult(boolean won) {
        playing = false;
        pauseButton.setVisible(false);
        pauseButton.setManaged(false);
        AudioManager.getInstance().stopMusic();
        AudioManager.getInstance().playEffect(
                AudioResources.fromName(won ? "win" : "lose"));
        gameResult.showResult(won);
    }

    private void setPaused(boolean paused) {
        if (!playing) {
            return;
        }

        if (gameScene.isPaused() != paused) {
            gameScene.togglePause();
        }
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
