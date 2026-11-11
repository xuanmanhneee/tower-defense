package com.towerdefense.ui;

import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class MainMenuView extends StackPane {

    private final VBox mainMenu;
    private final VBox settingsMenu;

    public MainMenuView(
            Runnable onStart,
            Consumer<Boolean> onShowFpsChanged,
            Runnable onExit) {

        getStyleClass().add("menu-overlay");
        setPrefSize(1280, 768);

        mainMenu = createMainMenu(onStart, onExit);
        settingsMenu = createSettingsMenu(onShowFpsChanged);
        settingsMenu.setVisible(false);
        settingsMenu.setManaged(false);

        getChildren().addAll(mainMenu, settingsMenu);
    }

    private VBox createMainMenu(Runnable onStart, Runnable onExit) {
        Label eyebrow = new Label("HOLD THE LINE");
        eyebrow.getStyleClass().add("menu-eyebrow");

        Label title = new Label("TOWER\nDEFENSE");
        title.getStyleClass().add("menu-title");

        Label subtitle = new Label("Build. Upgrade. Survive.");
        subtitle.getStyleClass().add("menu-subtitle");

        Button startButton = menuButton("START GAME", true);
        startButton.setOnAction(event -> {
            setVisible(false);
            setManaged(false);
            onStart.run();
        });

        Button settingsButton = menuButton("SETTINGS", false);
        settingsButton.setOnAction(event -> showSettings());

        Button exitButton = menuButton("EXIT", false);
        exitButton.getStyleClass().add("menu-button-danger");
        exitButton.setOnAction(event -> onExit.run());

        VBox buttons = new VBox(12, startButton, settingsButton, exitButton);
        buttons.setAlignment(Pos.CENTER);

        VBox card = new VBox(14, eyebrow, title, subtitle, buttons);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(480, 600);
        card.getStyleClass().add("menu-card");
        return card;
    }

    private VBox createSettingsMenu(Consumer<Boolean> onShowFpsChanged) {
        Label heading = new Label("SETTINGS");
        heading.getStyleClass().add("settings-title");

        Label description = new Label("DISPLAY");
        description.getStyleClass().add("settings-section-label");

        CheckBox showFps = new CheckBox("Show FPS counter");
        showFps.getStyleClass().add("settings-checkbox");
        showFps.setSelected(false);
        showFps.selectedProperty().addListener(
                (observable, previous, selected) -> onShowFpsChanged.accept(selected));

        Button backButton = menuButton("BACK", false);
        backButton.setOnAction(event -> showMenu());

        VBox card = new VBox(20, heading, description, showFps, backButton);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxSize(460, 410);
        card.getStyleClass().addAll("menu-card", "settings-card");
        return card;
    }

    private Button menuButton(String text, boolean primary) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        if (primary) {
            button.getStyleClass().add("menu-button-primary");
        }
        return button;
    }

    private void showSettings() {
        mainMenu.setVisible(false);
        mainMenu.setManaged(false);
        settingsMenu.setManaged(true);
        settingsMenu.setVisible(true);
    }

    public void showMenu() {
        settingsMenu.setVisible(false);
        settingsMenu.setManaged(false);
        mainMenu.setManaged(true);
        mainMenu.setVisible(true);
        setManaged(true);
        setVisible(true);
    }
}
