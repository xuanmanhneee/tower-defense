package com.towerdefense.ui;

import com.towerdefense.config.GameConfig;
import com.towerdefense.entity.tower.TowerType;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.function.Consumer;

public class TowerSelectionScreen implements UIScreen {

    private final List<TowerOption> towers;
    private final Consumer<TowerOption> onSelected;
    private TowerType selectedTower;

    private final double panelWidth = 260;
    private final double panelHeight = 230;

    public TowerSelectionScreen(
            List<TowerOption> towers,
            Consumer<TowerOption> onSelected
    ) {
        if (towers.isEmpty()) {
            throw new IllegalArgumentException("At least one tower option is required");
        }
        this.towers = towers;
        this.onSelected = onSelected;
        this.selectedTower = towers.get(0).type();
    }

    private double getPanelX() {
        return GameConfig.WINDOW_WIDTH - panelWidth - 20;
    }

    private double getPanelY() {
        return GameConfig.WINDOW_HEIGHT - panelHeight - 20;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {

        double panelX = getPanelX();
        double panelY = getPanelY();

        // Panel
        graphics.setFill(Color.rgb(30, 30, 30, 0.95));
        graphics.fillRoundRect(
                panelX,
                panelY,
                panelWidth,
                panelHeight,
                15,
                15
        );

        graphics.setFill(Color.WHITE);
        graphics.fillText(
                "Choose Tower",
                panelX + 20,
                panelY + 30
        );

        double y = panelY + 50;

        for (TowerOption tower : towers) {

            boolean selected = tower.type() == selectedTower;
            graphics.setFill(selected
                    ? Color.rgb(40, 115, 180)
                    : Color.rgb(60, 60, 60));
            graphics.fillRoundRect(
                    panelX + 15,
                    y,
                    panelWidth - 30,
                    35,
                    8,
                    8
            );

            graphics.setFill(Color.WHITE);
            graphics.fillText(
                    tower.name(),
                    panelX + 25,
                    y + 22
            );

            graphics.fillText(
                    "$" + tower.cost(),
                    panelX + 170,
                    y + 22
            );

            y += 40;
        }
    }

    @Override
    public boolean handleClick(double x, double y) {

        double panelX = getPanelX();
        double panelY = getPanelY();

        if (x < panelX || x > panelX + panelWidth
                || y < panelY || y > panelY + panelHeight) {
            return false;
        }

        double optionY = panelY + 50;

        for (TowerOption tower : towers) {

            if (x >= panelX + 15
                    && x <= panelX + panelWidth - 15
                    && y >= optionY
                    && y <= optionY + 35) {

                selectedTower = tower.type();
                onSelected.accept(tower);
                return true;
            }

            optionY += 40;
        }

        return true;
    }

    public TowerType getSelectedTower() {
        return selectedTower;
    }
}
