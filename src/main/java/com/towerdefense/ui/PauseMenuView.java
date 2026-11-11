package com.towerdefense.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class PauseMenuView extends StackPane {

    public PauseMenuView(Runnable onResume, Runnable onExit) {
        getStyleClass().add("pause-overlay");
        setPrefSize(1280, 768);

        Label eyebrow = new Label("GAME PAUSED");
        eyebrow.getStyleClass().add("menu-eyebrow");

        Label title = new Label("PAUSED");
        title.getStyleClass().add("pause-title");

        Label hint = new Label("Take a breath. The battlefield will wait.");
        hint.getStyleClass().add("menu-subtitle");

        Button resume = new Button("RESUME");
        resume.getStyleClass().addAll("menu-button", "menu-button-primary");
        resume.setOnAction(event -> onResume.run());

        Button exit = new Button("EXIT GAME");
        exit.getStyleClass().addAll("menu-button", "menu-button-danger");
        exit.setOnAction(event -> onExit.run());

        Label shortcut = new Label("Press ESC to resume");
        shortcut.getStyleClass().add("pause-shortcut");

        VBox card = new VBox(14, eyebrow, title, hint, resume, exit, shortcut);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(460, 460);
        card.getStyleClass().addAll("menu-card", "pause-card");
        getChildren().add(card);

        hideMenu();
    }

    public void showMenu() {
        setManaged(true);
        setVisible(true);
    }

    public void hideMenu() {
        setVisible(false);
        setManaged(false);
    }
}
