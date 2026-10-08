package com.towerdefense.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.function.Consumer;

public class TowerSelectionScreen implements UIScreen {

    private final List<TowerOption> towers;
    private final Consumer<TowerOption> onSelected;

    private final double panelWidth = 250;
    private final double panelHeight = 180;

    public TowerSelectionScreen(
            List<TowerOption> towers,
            Consumer<TowerOption> onSelected
    ) {
        this.towers = towers;
        this.onSelected = onSelected;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext graphics) {

        double panelX = 800 - panelWidth - 20;
        double panelY = 200;

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

            graphics.setFill(Color.rgb(60, 60, 60));
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
    public void handleClick(double x, double y) {

        double panelX = 800 - panelWidth - 20;
        double panelY = 200;

        double optionY = panelY + 50;

        for (TowerOption tower : towers) {

            if (x >= panelX + 15
                    && x <= panelX + panelWidth - 15
                    && y >= optionY
                    && y <= optionY + 35) {

                onSelected.accept(tower);
                return;
            }

            optionY += 40;
        }
    }
}