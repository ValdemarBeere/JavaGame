package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.controller.MusicController;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * View class for Main Menu.
 */
public class OpeningView extends VBox {

  private final Button startButton;

  /**
   * Constructor for the OpeningView class. Initializes the components of the UI and arranges them
   * on the stage.
   *
   * @param primaryStage the primary stage on which the UI components are arranged
   */
  public OpeningView(Stage primaryStage) {
    MediaPlayer menuMusic = MusicController.getInstance().getMenuMusic();
    menuMusic.play();

    getChildren().add(new UniversalMenuBar());
    setPrefSize(1600, 900);
    getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/background.css")).toExternalForm());
    getStyleClass().add("background");

    GridPane gridPane = new GridPane();
    gridPane.setPrefSize(1600, 900);

    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setHgrow(Priority.SOMETIMES);
    columnConstraints.setMinWidth(10);
    columnConstraints.setPrefWidth(100);
    gridPane.getColumnConstraints().add(columnConstraints);

    for (int i = 0; i < 3; i++) {
      RowConstraints rowConstraints = new RowConstraints();
      rowConstraints.setMinHeight(10);
      rowConstraints.setPrefHeight(30);
      rowConstraints.setVgrow(Priority.SOMETIMES);
      gridPane.getRowConstraints().add(rowConstraints);
    }

    ImageView titleImageView = new ImageView();
    titleImageView.setFitWidth(800);
    titleImageView.setFitHeight(172);
    titleImageView.setImage(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/text/title.png"))));
    GridPane.setHalignment(titleImageView, javafx.geometry.HPos.CENTER);
    GridPane.setValignment(titleImageView, javafx.geometry.VPos.CENTER);
    DropShadow titleShadow = new DropShadow();
    titleShadow.setSpread(0.78);
    titleShadow.setColor(new Color(1, 0.9173611402511597, 0.44907405972480774, 1));
    titleImageView.setEffect(titleShadow);

    startButton = new Button();
    startButton.setId("beginPathsButton");
    startButton.setAlignment(Pos.CENTER);
    startButton.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm());
    startButton.getStyleClass().add("transparentButton");
    GridPane.setHalignment(startButton, javafx.geometry.HPos.CENTER);
    GridPane.setValignment(startButton, javafx.geometry.VPos.CENTER);
    GridPane.setRowIndex(startButton, 1);

    ImageView startButtonImageView = new ImageView();
    startButtonImageView.setFitWidth(400);
    startButtonImageView.setFitHeight(82);
    startButtonImageView.setImage(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/text/start.png"))));
    DropShadow buttonShadow = new DropShadow();
    buttonShadow.setSpread(0.5);
    buttonShadow.setColor(Color.WHITE);
    startButtonImageView.setEffect(buttonShadow);
    startButton.setGraphic(startButtonImageView);

    gridPane.add(titleImageView, 0, 0);
    gridPane.add(startButton, 0, 1);

    getChildren().add(gridPane);

    primaryStage.setTitle("Non-linear Story Game");
  }

  public void setStartButtonAction(EventHandler<ActionEvent> action) {
    startButton.setOnAction(action);
  }
}
