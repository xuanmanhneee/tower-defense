package com.towerdefense.render;

import com.towerdefense.entity.tower.TowerType;
import javafx.scene.paint.Color;

public final class TowerVisuals {

    private TowerVisuals() {
    }

    public static Color colorFor(TowerType type) {
        return switch (type) {
            case BASIC -> Color.DODGERBLUE;
            case SPREAD -> Color.MEDIUMPURPLE;
            case SLOW -> Color.LIGHTSEAGREEN;
        };
    }
}