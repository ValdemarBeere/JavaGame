package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.controller.MusicController;
import java.util.Objects;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

/**
 * View class for Victory.
 */
public class VictoryView extends VBox {

  /**
   * Constructor for the VictoryView class. Initializes the components of the UI and arranges them
   * on the stage.
   */
  public VictoryView() {
    getChildren().add(new UniversalMenuBar());

    MusicController.getInstance().getMenuMusic().pause();
    MediaPlayer victoryMusic = MusicController.getInstance().getVictoryMusic();
    victoryMusic.setOnEndOfMedia(() -> {
      victoryMusic.stop();
      MusicController.getInstance().getMenuMusic().play();
    });
    victoryMusic.play();

    setPrefSize(1600, 900);
    getStylesheets().add(
        Objects.requireNonNull(VictoryView.class.getResource("/styles/background.css"))
            .toExternalForm());
    getStyleClass().add("background");

    GridPane mainGrid = new GridPane();
    mainGrid.setPrefSize(1600, 900);
    RowConstraints Row1 = new RowConstraints();
    Row1.setPercentHeight(50);
    RowConstraints Row2 = new RowConstraints();
    Row2.setPercentHeight(50);
    ColumnConstraints Column1 = new ColumnConstraints();
    Column1.setPercentWidth(100);
    mainGrid.getColumnConstraints().add(Column1);
    mainGrid.getRowConstraints().addAll(Row1, Row2);
    getChildren().add(mainGrid);

    GridPane buttonGrid = new GridPane();
    ColumnConstraints buttonColumn1 = new ColumnConstraints();
    buttonColumn1.setPercentWidth(50);
    ColumnConstraints buttonColumn2 = new ColumnConstraints();
    buttonColumn2.setPercentWidth(50);
    RowConstraints buttonRow1 = new RowConstraints();
    buttonRow1.setPercentHeight(100);
    buttonGrid.getColumnConstraints().addAll(buttonColumn1, buttonColumn1);
    buttonGrid.getRowConstraints().add(buttonRow1);
    GridPane.setConstraints(buttonGrid, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    mainGrid.getChildren().add(buttonGrid);

    Label victoryLabel = new Label("Victory");
    victoryLabel.getStyleClass().add("largeText");
    victoryLabel.getStylesheets().add(
        Objects.requireNonNull(VictoryView.class.getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(victoryLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    mainGrid.getChildren().add(victoryLabel);

    Button homeButton = new Button("Home");
    homeButton.getStyleClass().addAll("transparentButton", "continueText");
    homeButton.getStylesheets().addAll(
        Objects.requireNonNull(VictoryView.class.getResource("/styles/buttons.css"))
            .toExternalForm(),
        Objects.requireNonNull(VictoryView.class.getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(homeButton, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    homeButton.setPadding(new Insets(1, 1, 1, 1));
    homeButton.setOnAction(e -> Main.switchToOpeningView());

    Button exitButton = new Button("Exit");
    exitButton.getStyleClass().addAll("transparentButton", "continueText");
    exitButton.getStylesheets().addAll(
        Objects.requireNonNull(VictoryView.class.getResource("/styles/buttons.css"))
            .toExternalForm(),
        Objects.requireNonNull(VictoryView.class.getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(exitButton, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    exitButton.setPadding(new Insets(1, 1, 1, 1));
    exitButton.setOnAction(e -> Platform.exit());

    buttonGrid.getChildren().addAll(homeButton, exitButton);
  }
}
