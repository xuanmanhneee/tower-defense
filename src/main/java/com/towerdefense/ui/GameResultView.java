package com.towerdefense.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class GameResultView extends StackPane {

    private final Label eyebrow = new Label();
    private final Label title = new Label();

    public GameResultView(Runnable onReplay, Runnable onMainMenu) {
        getStyleClass().add("pause-overlay");
        setPrefSize(1280, 768);

        eyebrow.getStyleClass().add("menu-eyebrow");
        title.getStyleClass().add("result-title");

        Button replay = new Button("PLAY AGAIN");
        replay.getStyleClass().addAll("menu-button", "menu-button-primary");
        replay.setOnAction(event -> onReplay.run());

        Button mainMenu = new Button("MAIN MENU");
        mainMenu.getStyleClass().add("menu-button");
        mainMenu.setOnAction(event -> onMainMenu.run());

        VBox card = new VBox(18, eyebrow, title, replay, mainMenu);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(460, 420);
        card.getStyleClass().addAll("menu-card", "pause-card");
        getChildren().add(card);

        hideResult();
    }

    public void showResult(boolean won) {
        eyebrow.setText(won ? "ALL WAVES CLEARED" : "THE BASE HAS FALLEN");
        title.setText(won ? "VICTORY" : "DEFEAT");
        setManaged(true);
        setVisible(true);
    }

    public void hideResult() {
        setVisible(false);
        setManaged(false);
    }
}
