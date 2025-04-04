package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.controller.CreatePlayerController;
import edu.ntnu.idatt2001.paths.model.exceptions.PlayerValidationException;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * View class for CreatePlayer.
 */
public class CreatePlayerView extends VBox {

  private final CreatePlayerController controller = new CreatePlayerController();
  private final ImageView playerImageView;
  private final ChoiceBox<String> raceMenu;
  private final Map<String, File> fileMap = new HashMap<>();

  /**
   * Constructor for the CreatePlayerView class. Initializes the components of the UI and arranges
   * them on the stage.
   *
   * @param stage the primary stage on which the UI components are arranged
   */
  public CreatePlayerView(Stage stage) {
    playerImageView = new ImageView();
    playerImageView.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/images/playerImages/human.png"))));
    playerImageView.setPickOnBounds(true);
    playerImageView.setPreserveRatio(true);
    playerImageView.setFitWidth(800);
    playerImageView.setEffect(new DropShadow(10, Color.WHITE));
    GridPane.setConstraints(playerImageView, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

    this.raceMenu = new ChoiceBox<>();
    raceMenu.getStyleClass().addAll("textField", "textFieldText");
    raceMenu.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    raceMenu.setPrefWidth(480);
    GridPane.setConstraints(raceMenu, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

    loadImages();
    controller.setGameStarter((v) -> {
      // Switch to the next scene here
      SelectStoryView selectStoryView = new SelectStoryView(stage);
      Main.switchScene(selectStoryView);
    });
    createScene();
  }

  /**
   * Loads images and adds them to raceMenu.
   */
  public void loadImages() {
    File imageDirectory = new File(
        Objects.requireNonNull(getClass().getResource("/images/playerImages")).getFile());
    Arrays.stream(Objects.requireNonNull(imageDirectory.listFiles())).forEach(file -> {
      String fileName = file.getName().replaceAll(".png", "");
      raceMenu.getItems().add(fileName);
      fileMap.put(fileName, file);
    });
  }

  /**
   * Initializes other components of the UI and arranges them on the stage.
   */
  public void createScene() {

    getChildren().add(new UniversalMenuBar());
    setPrefSize(1600, 900);
    getStyleClass().add("background");
    getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/background.css")).toExternalForm());

    GridPane mainGrid = new GridPane();
    mainGrid.setPrefSize(1600, 900);
    ColumnConstraints mainColumn1 = new ColumnConstraints();
    mainColumn1.setPercentWidth(50);
    ColumnConstraints mainColumn2 = new ColumnConstraints();
    mainColumn2.setPercentWidth(50);
    RowConstraints mainRow1 = new RowConstraints();
    mainRow1.setPercentHeight(100);
    mainGrid.getColumnConstraints().addAll(mainColumn1, mainColumn1);
    mainGrid.getRowConstraints().add(mainRow1);
    getChildren().add(mainGrid);

    GridPane nestedGrid = new GridPane();
    RowConstraints nestedRow1 = new RowConstraints();
    CreateStoryView.initiateLinkText(nestedGrid, nestedRow1);
    mainGrid.getChildren().add(nestedGrid);

    GridPane nameGrid = new GridPane();
    CreateStoryView.initiatePane(nameGrid);
    nestedGrid.getChildren().add(nameGrid);

    GridPane raceGrid = new GridPane();
    CreateStoryView.initiatePane(raceGrid);
    GridPane.setConstraints(raceGrid, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    nestedGrid.getChildren().add(raceGrid);

    Button continueButton = new Button("Continue");
    continueButton.getStyleClass().addAll("transparentButton", "continueText");
    continueButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    continueButton.setPadding(new Insets(1, 1, 1, 1));
    GridPane.setConstraints(continueButton, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    nestedGrid.getChildren().add(continueButton);

    Label nameLabel = new Label("Name");
    nameLabel.getStyleClass().add("largeText");
    nameLabel.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(nameLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);

    TextField nameField = new TextField();
    nameField.setTextFormatter(
        new TextFormatter<>(TextFieldConsumers.getAlphabeticalAndSizeFilter()));

    nameField.getStyleClass().addAll("textField", "textFieldText");
    nameField.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(nameField, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

    nameGrid.getChildren().addAll(nameLabel, nameField);

    Label raceLabel = new Label("Race");
    raceLabel.getStyleClass().add("largeText");
    raceLabel.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(raceLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);

    raceGrid.getChildren().addAll(raceLabel, raceMenu);

    mainGrid.getChildren().add(playerImageView);

    raceMenu.setValue("human");

    raceMenu.setConverter(new StringConverter<>() {
      @Override
      public String toString(String string) {
        return string;
      }

      @Override
      public String fromString(String string) {
        return string;
      }
    });

    raceMenu.setOnAction(event -> {
      String selectedFileName = raceMenu.getSelectionModel().getSelectedItem();
      if (selectedFileName != null) {
        File selectedFile = fileMap.get(selectedFileName);
        playerImageView.setImage(new Image(selectedFile.toURI().toString()));
      }
    });

    continueButton.setOnAction(event -> {
      try {
        controller.createPlayer(nameField.getText(), playerImageView.getImage());
      } catch (PlayerValidationException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText("Validation Error:");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        nameField.clear();
      }
    });
  }
}
