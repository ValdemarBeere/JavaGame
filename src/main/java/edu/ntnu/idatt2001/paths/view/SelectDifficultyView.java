package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.controller.SelectDifficultyController;
import edu.ntnu.idatt2001.paths.model.game.GameDifficultyEnum;
import java.util.Objects;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * View class for SelectDifficulty.
 */
public class SelectDifficultyView extends VBox {

  private SelectDifficultyController selectDifficultyController;

  /**
   * Constructor for the SelectDifficultyView class. Initializes the components of the UI and
   * arranges them on the stage.
   *
   * @param primaryStage the primary stage on which the UI components are arranged
   */
  public SelectDifficultyView(Stage primaryStage) {
    getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/background.css")).toExternalForm());
    getStyleClass().add("background");
    getChildren().add(new UniversalMenuBar());
    setPrefSize(1600, 900);
    GridPane gridPane = new GridPane();
    gridPane.setPrefSize(1600, 900);

    ColumnConstraints columnConstraints = new ColumnConstraints(100, 10, Double.MAX_VALUE);
    columnConstraints.setHgrow(Priority.SOMETIMES);
    gridPane.getColumnConstraints().add(columnConstraints);

    for (int i = 0; i < 3; i++) {
      RowConstraints rowConstraints = new RowConstraints(30, 10, Double.MAX_VALUE);
      rowConstraints.setVgrow(Priority.SOMETIMES);
      gridPane.getRowConstraints().add(rowConstraints);
    }

    Label label = new Label("Select difficulty");
    label.getStyleClass().add("largeText");
    label.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setHalignment(label, HPos.CENTER);
    GridPane.setValignment(label, VPos.CENTER);
    gridPane.add(label, 0, 0);

    ChoiceBox<GameDifficultyEnum> chooseDifficulty = new ChoiceBox<>();
    chooseDifficulty.getStyleClass().addAll("textField", "textFieldText");
    chooseDifficulty.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    chooseDifficulty.getItems().addAll(GameDifficultyEnum.values());
    chooseDifficulty.setPrefWidth(635);

    GridPane.setHalignment(chooseDifficulty, HPos.CENTER);
    GridPane.setValignment(chooseDifficulty, VPos.CENTER);
    gridPane.add(chooseDifficulty, 0, 1);

    // setup UI bindings
    chooseDifficulty.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldValue, newValue) -> {
          if (newValue != null) {
            selectDifficultyController.difficultySelected(newValue);
          }
        });

    selectDifficultyController = new SelectDifficultyController();
    selectDifficultyController.setGameStarter((difficulty) -> {
      // transition to GameView
      GameView gameView = new GameView(primaryStage);
      Main.switchScene(gameView);
    });

    getChildren().add(gridPane);
    primaryStage.setTitle("Non-linear Story Game");
  }

}
