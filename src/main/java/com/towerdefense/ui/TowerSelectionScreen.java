package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import com.towerdefense.entity.tower.TowerType;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.function.Consumer;

public class TowerSelectionScreen implements UIScreen {

    private static final double PANEL_WIDTH = 900;
    private static final double PANEL_HEIGHT = 70;
    private static final double PANEL_X = (GameConfig.WINDOW_WIDTH - PANEL_WIDTH) / 2.0;
    private static final double PANEL_Y = GameConfig.WINDOW_HEIGHT - PANEL_HEIGHT - 16;
    private static final double OPTIONS_X = PANEL_X + 122;
    private static final double OPTION_Y = PANEL_Y + 10;
    private static final double OPTION_WIDTH = 180;
    private static final double OPTION_HEIGHT = 50;
    private static final double OPTION_GAP = 7;

    private final List<TowerOption> towers;
    private final Consumer<TowerOption> onSelected;
    private TowerType selectedTower;
    private TowerType hoveredTower;

    public TowerSelectionScreen(List<TowerOption> towers, Consumer<TowerOption> onSelected) {
        if (towers.isEmpty()) {
            throw new IllegalArgumentException("At least one tower option is required");
        }
        this.towers = towers;
        this.onSelected = onSelected;
        this.selectedTower = towers.get(0).type();
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {
        graphics.setFill(Color.rgb(5, 15, 12, 0.95));
        graphics.fillRoundRect(PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, 18, 18);
        graphics.setStroke(Color.web("#315c47"));
        graphics.setLineWidth(1);
        graphics.strokeRoundRect(PANEL_X + 0.5, PANEL_Y + 0.5,
                PANEL_WIDTH - 1, PANEL_HEIGHT - 1, 18, 18);

        graphics.setFill(Color.web("#78e3a3"));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 9));
        graphics.fillText("ARSENAL", PANEL_X + 18, PANEL_Y + 25);
        graphics.setFill(Color.web("#effbf3"));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        graphics.fillText("TOWERS", PANEL_X + 18, PANEL_Y + 46);

        double optionX = OPTIONS_X;
        for (TowerOption tower : towers) {
            drawOption(graphics, tower, optionX, OPTION_Y);
            optionX += OPTION_WIDTH + OPTION_GAP;
        }
    }

    private void drawOption(GraphicsContext graphics, TowerOption tower, double x, double y) {
        boolean selected = tower.type() == selectedTower;
        boolean hovered = tower.type() == hoveredTower;

        Color background = selected
                ? Color.web("#1f5b40")
                : hovered ? Color.web("#182f26") : Color.web("#101e19");
        Color border = selected ? Color.web("#76e0a1") : Color.web("#294238");

        graphics.setFill(background);
        graphics.fillRoundRect(x, y, OPTION_WIDTH, OPTION_HEIGHT, 10, 10);
        graphics.setStroke(border);
        graphics.strokeRoundRect(x + 0.5, y + 0.5,
                OPTION_WIDTH - 1, OPTION_HEIGHT - 1, 10, 10);

        if (selected) {
            graphics.setFill(Color.web("#76e0a1"));
            graphics.fillRoundRect(x, y + 8, 4, OPTION_HEIGHT - 16, 4, 4);
        }

        graphics.setFill(Color.web("#effbf3"));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        graphics.fillText(tower.name(), x + 13, y + 20);

        graphics.setFill(Color.web("#829b8e"));
        graphics.setFont(Font.font("Segoe UI", 8));
        graphics.fillText(tower.description(), x + 13, y + 37);

        graphics.setFill(selected ? Color.web("#b8f5cf") : Color.web("#d9e8df"));
        graphics.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        graphics.fillText("$" + tower.cost(), x + OPTION_WIDTH - 38, y + 21);
    }

    @Override
    public boolean handleClick(double x, double y) {
        int optionIndex = optionIndexAt(x, y);
        if (optionIndex >= 0) {
            TowerOption tower = towers.get(optionIndex);
            selectedTower = tower.type();
            onSelected.accept(tower);
            return true;
        }

        return x >= PANEL_X && x <= PANEL_X + PANEL_WIDTH
                && y >= PANEL_Y && y <= PANEL_Y + PANEL_HEIGHT;
    }

    @Override
    public void handleMouseMove(double x, double y) {
        int optionIndex = optionIndexAt(x, y);
        hoveredTower = optionIndex >= 0 ? towers.get(optionIndex).type() : null;
    }

    private int optionIndexAt(double x, double y) {
        if (y < OPTION_Y || y > OPTION_Y + OPTION_HEIGHT) {
            return -1;
        }

        double optionX = OPTIONS_X;
        for (int i = 0; i < towers.size(); i++) {
            if (x >= optionX && x <= optionX + OPTION_WIDTH) {
                return i;
            }
            optionX += OPTION_WIDTH + OPTION_GAP;
        }
        return -1;
    }

    public TowerType getSelectedTower() {
        return selectedTower;
    }
}
