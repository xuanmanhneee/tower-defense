package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import com.towerdefense.debug.DebugMetrics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class DebugPanelScreen implements UIScreen {

    private static final double PANEL_WIDTH = 310;
    private static final double PANEL_HEIGHT = 126;
    private static final double SCREEN_MARGIN = 20;
    private static final double REFRESH_INTERVAL = 0.5;
    private static final double SWITCH_WIDTH = 48;
    private static final double SWITCH_HEIGHT = 22;

    private final DebugMetrics metrics;
    private boolean visible;
    private double refreshElapsed;
    private long displayedCreated;
    private long displayedReused;

    public DebugPanelScreen(DebugMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public void update(double deltaTime) {
        if (deltaTime <= 0) {
            return;
        }

        refreshElapsed += deltaTime;

        if (refreshElapsed >= REFRESH_INTERVAL) {
            refreshElapsed = 0;
            refreshObjectCounters();
        }
    }

    @Override
    public void render(GraphicsContext graphics) {
        if (!visible) {
            return;
        }

        double panelX = GameConfig.WINDOW_WIDTH - PANEL_WIDTH - SCREEN_MARGIN;
        double panelY = GameConfig.WINDOW_HEIGHT - PANEL_HEIGHT - SCREEN_MARGIN;

        graphics.setFill(Color.rgb(15, 18, 22, 0.92));
        graphics.fillRoundRect(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 12, 12);

        graphics.setFill(Color.web("#80cbc4"));
        graphics.fillText("OBJECT POOL  [F3 hide]", panelX + 14, panelY + 22);

        graphics.setFill(Color.WHITE);
        graphics.fillText("Runtime pooling", panelX + 14, panelY + 52);

        renderPoolSwitch(graphics, switchX(panelX), switchY(panelY));

        graphics.setFill(Color.WHITE);
        graphics.fillText("New objects: " + displayedCreated, panelX + 14, panelY + 82);
        graphics.fillText("Reused objects: " + displayedReused, panelX + 14, panelY + 104);
    }

    @Override
    public boolean handleClick(double x, double y) {
        if (!visible) {
            return false;
        }

        double panelX = GameConfig.WINDOW_WIDTH - PANEL_WIDTH - SCREEN_MARGIN;
        double panelY = GameConfig.WINDOW_HEIGHT - PANEL_HEIGHT - SCREEN_MARGIN;

        if (x < panelX || x > panelX + PANEL_WIDTH
                || y < panelY || y > panelY + PANEL_HEIGHT) {
            return false;
        }

        double switchX = switchX(panelX);
        double switchY = switchY(panelY);
        if (x >= switchX && x <= switchX + SWITCH_WIDTH
                && y >= switchY && y <= switchY + SWITCH_HEIGHT) {
            metrics.toggleObjectPool();
            refreshObjectCounters();
        }

        return true;
    }

    public void handleKeyPressed(KeyCode keyCode) {
        if (keyCode == KeyCode.F3) {
            visible = !visible;
            if (visible) {
                refreshObjectCounters();
            }
        }
    }

    private void refreshObjectCounters() {
        displayedCreated = metrics.getCreatedObjectCount();
        displayedReused = metrics.getReusedObjectCount();
    }

    private void renderPoolSwitch(GraphicsContext graphics, double x, double y) {
        boolean enabled = metrics.isObjectPoolEnabled();
        graphics.setFill(enabled ? Color.web("#43a047") : Color.web("#616161"));
        graphics.fillRoundRect(x, y, SWITCH_WIDTH, SWITCH_HEIGHT, SWITCH_HEIGHT, SWITCH_HEIGHT);

        double knobDiameter = SWITCH_HEIGHT - 4;
        double knobX = enabled ? x + SWITCH_WIDTH - knobDiameter - 2 : x + 2;
        graphics.setFill(Color.WHITE);
        graphics.fillOval(knobX, y + 2, knobDiameter, knobDiameter);

        graphics.setFill(enabled ? Color.web("#81c784") : Color.web("#ef9a9a"));
        graphics.fillText(enabled ? "ON" : "OFF", x - 31, y + 16);
    }

    private double switchX(double panelX) {
        return panelX + PANEL_WIDTH - SWITCH_WIDTH - 14;
    }

    private double switchY(double panelY) {
        return panelY + 35;
    }
}
