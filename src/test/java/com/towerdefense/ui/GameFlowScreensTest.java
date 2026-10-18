package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import com.towerdefense.game.GameSessionStats;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFlowScreensTest {

    @Test
    void mainMenuRunsOnlyClickedActionAndLeavesBackdropUnhandled() {
        AtomicInteger plays = new AtomicInteger();
        AtomicInteger quits = new AtomicInteger();
        MainMenuScreen screen = new MainMenuScreen(plays::incrementAndGet, quits::incrementAndGet);

        clickButton(screen, MainMenuScreen.buttonTopY(0));
        assertEquals(1, plays.get());
        assertEquals(0, quits.get());

        clickButton(screen, MainMenuScreen.buttonTopY(1));
        assertEquals(1, plays.get());
        assertEquals(1, quits.get());

        assertFalse(screen.handleClick(10, 10));
        assertEquals(1, plays.get());
        assertEquals(1, quits.get());
    }

    @Test
    void pauseMenuRunsButtonCallbacksAndConsumesBackdrop() {
        AtomicInteger resumes = new AtomicInteger();
        AtomicInteger restarts = new AtomicInteger();
        AtomicInteger menus = new AtomicInteger();
        PauseMenuScreen screen = new PauseMenuScreen(
                resumes::incrementAndGet,
                restarts::incrementAndGet,
                menus::incrementAndGet);

        clickButton(screen, PauseMenuScreen.buttonTopY(0));
        assertEquals(1, resumes.get());
        assertEquals(0, restarts.get());
        assertEquals(0, menus.get());

        clickButton(screen, PauseMenuScreen.buttonTopY(1));
        assertEquals(1, restarts.get());
        assertEquals(0, menus.get());

        clickButton(screen, PauseMenuScreen.buttonTopY(2));
        assertEquals(1, menus.get());

        assertTrue(screen.handleClick(10, 10));
        assertEquals(1, resumes.get());
        assertEquals(1, restarts.get());
        assertEquals(1, menus.get());
    }

    @Test
    void gameOverMenuRunsButtonCallbacksAndConsumesBackdrop() {
        AtomicInteger restarts = new AtomicInteger();
        AtomicInteger menus = new AtomicInteger();
        GameOverScreen screen = new GameOverScreen(
                GameSessionStats::new,
                restarts::incrementAndGet,
                menus::incrementAndGet);

        clickButton(screen, GameOverScreen.buttonTopY(0));
        assertEquals(1, restarts.get());
        assertEquals(0, menus.get());

        clickButton(screen, GameOverScreen.buttonTopY(1));
        assertEquals(1, menus.get());

        assertTrue(screen.handleClick(10, 10));
        assertEquals(1, restarts.get());
        assertEquals(1, menus.get());
    }

    @Test
    void modalBackdropDoesNotForwardClickToUnderlyingScreen() {
        AtomicInteger underlyingClicks = new AtomicInteger();
        UIManager manager = new UIManager();
        manager.addScreen(new RecordingScreen(underlyingClicks));
        manager.addScreen(new PauseMenuScreen(() -> {
        }, () -> {
        }, () -> {
        }));

        assertTrue(manager.handleClick(10, 10));
        assertEquals(0, underlyingClicks.get());
    }

    @Test
    void menuButtonIncludesVisibleBoundaryAndIgnoresOutsideClick() {
        AtomicInteger clicks = new AtomicInteger();
        MenuButton button = new MenuButton(
                10, 20, 100, 40, "Test", MenuButton.Variant.PRIMARY, clicks::incrementAndGet);

        assertTrue(button.handleClick(110, 60));
        assertEquals(1, clicks.get());
        assertFalse(button.handleClick(111, 60));
        assertEquals(1, clicks.get());
    }

    private static void clickButton(UIScreen screen, double topY) {
        double x = (GameConfig.WINDOW_WIDTH - 280) / 2.0 + 140;
        assertTrue(screen.handleClick(x, topY + 24));
    }

    private static final class RecordingScreen implements UIScreen {
        private final AtomicInteger clicks;

        private RecordingScreen(AtomicInteger clicks) {
            this.clicks = clicks;
        }

        @Override
        public void update(double deltaTime) {
        }

        @Override
        public void render(GraphicsContext graphics) {
        }

        @Override
        public boolean handleClick(double x, double y) {
            clicks.incrementAndGet();
            return false;
        }
    }
}
