package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.controller.CreateStoryController;
import edu.ntnu.idatt2001.paths.controller.EditStoryController;
import edu.ntnu.idatt2001.paths.controller.SelectStoryController;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.StoryFileManager;
import edu.ntnu.idatt2001.paths.model.exceptions.PathsValidationException;
import edu.ntnu.idatt2001.paths.model.validations.PathsValidator;
import java.io.File;
import java.util.Collection;
import java.util.Objects;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * View class for SelectStory.
 */
public class SelectStoryView extends VBox {

  private final SelectStoryController selectStoryController;

  private final ChoiceBox<String> chooseStory;
  private final FileChooser fileChooser;
  private final Button confirmButton;
  private final Button editStoryButton;
  private String story;


  /**
   * Constructor for the SelectStoryView class. Initializes the components of the UI and arranges
   * them on the stage.
   *
   * @param primaryStage the primary stage on which the UI components are arranged
   */
  public SelectStoryView(Stage primaryStage) {

    fileChooser = new FileChooser();

    getChildren().add(new UniversalMenuBar());
    setPrefSize(1600, 900);
    getStyleClass().add("background");
    getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/background.css")).toExternalForm());

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

    GridPane nestedGrid = new GridPane();
    ColumnConstraints nestedColumn1 = new ColumnConstraints();
    nestedColumn1.setPercentWidth(33.33);
    ColumnConstraints nestedColumn2 = new ColumnConstraints();
    nestedColumn2.setPercentWidth(33.33);
    ColumnConstraints nestedColumn3 = new ColumnConstraints();
    nestedColumn2.setPercentWidth(33.34);
    RowConstraints nestedRow1 = new RowConstraints();
    nestedRow1.setPercentHeight(100);
    nestedGrid.getColumnConstraints().addAll(nestedColumn1, nestedColumn2, nestedColumn3);
    nestedGrid.getRowConstraints().add(nestedRow1);
    GridPane.setConstraints(nestedGrid, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(nestedGrid);

    Label titleLabel = new Label("Select a story");
    GridPane.setHalignment(titleLabel, HPos.CENTER);
    GridPane.setValignment(titleLabel, VPos.CENTER);
    titleLabel.getStyleClass().add("largeText");
    titleLabel.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    gridPane.add(titleLabel, 0, 0);

    Button uploadStoryButton = new Button("Upload story");
    GridPane.setHalignment(uploadStoryButton, HPos.CENTER);
    GridPane.setValignment(uploadStoryButton, VPos.CENTER);
    uploadStoryButton.setPadding(new Insets(1, 1, 1, 1));
    uploadStoryButton.getStyleClass().addAll("transparentButton", "continueText");
    uploadStoryButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm(),
        Objects.requireNonNull(
            getClass().getResource("/styles/text.css")).toExternalForm());
    nestedGrid.add(uploadStoryButton, 0, 0);

    Button createNewStoryButton = new Button("Create Story");
    GridPane.setHalignment(createNewStoryButton, HPos.CENTER);
    GridPane.setValignment(createNewStoryButton, VPos.CENTER);
    createNewStoryButton.setPadding(new Insets(1, 1, 1, 1));
    createNewStoryButton.getStyleClass().addAll("transparentButton", "continueText");
    createNewStoryButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm(),
        Objects.requireNonNull(
            getClass().getResource("/styles/text.css")).toExternalForm());
    nestedGrid.add(createNewStoryButton, 2, 0);

    editStoryButton = new Button("Edit Story");
    GridPane.setHalignment(editStoryButton, HPos.CENTER);
    GridPane.setValignment(editStoryButton, VPos.CENTER);
    editStoryButton.setPadding(new Insets(1, 1, 1, 1));
    editStoryButton.getStyleClass().addAll("transparentButton", "continueText");
    editStoryButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm(),
        Objects.requireNonNull(
            getClass().getResource("/styles/text.css")).toExternalForm());
    nestedGrid.add(editStoryButton, 1, 0);
    editStoryButton.setVisible(false);

    confirmButton = new Button("Confirm Story");
    GridPane.setHalignment(confirmButton, HPos.CENTER);
    GridPane.setValignment(confirmButton, VPos.CENTER);
    confirmButton.setPadding(new Insets(1, 1, 1, 1));
    confirmButton.getStyleClass().addAll("transparentButton", "continueText");
    confirmButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm(),
        Objects.requireNonNull(
            getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setMargin(confirmButton, new Insets(300, 0, 0, 0));
    gridPane.add(confirmButton, 0, 1);
    confirmButton.setVisible(false);

    chooseStory = new ChoiceBox<>();
    chooseStory.setPrefSize(600, 79);
    GridPane.setHalignment(chooseStory, HPos.CENTER);
    GridPane.setValignment(chooseStory, VPos.CENTER);
    chooseStory.getStyleClass().addAll("textField", "textFieldText");
    chooseStory.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());

    gridPane.add(chooseStory, 0, 1);

    getChildren().add(gridPane);
    setupUIBindings();

    selectStoryController = new SelectStoryController(this);
    selectStoryController.setGameStarter((story) -> {
      SelectDifficultyView selectDifficultyView = new SelectDifficultyView(primaryStage);
      Main.switchScene(selectDifficultyView);
    });

    uploadStoryButton.setOnAction(e -> {
      if (chooseStory.getItems().size() >= 8) {
        String msg = ("""
            Remove an item from the list.
            To remove an item form the list
            Right click the element you want to delete""");
        informationAlert(msg);
        return;
      }
      File file = fileChooser.showOpenDialog(null);
      if (file != null) {
        try {
          // Validate the image file
          PathsValidator.validatePathsFile(file);
        } catch (PathsValidationException ex) {
          handleException(ex);
          return;
        }
        selectStoryController.saveStoryToFolder(file);
        selectStoryController.initialize();
        chooseStory.getItems().clear();
        chooseStory.getItems().addAll(selectStoryController.getStoryTitleToFileMap().keySet());
      }
    });

    createNewStoryButton.setOnAction(e -> {
      if (chooseStory.getItems().size() >= 8) {
        String msg = ("""
            Remove an item from the list.
            To remove an item form the list
            Right click the element you want to delete""");
        informationAlert(msg);
        return;
      }
      StoryFileManager fileManager = new StoryFileManager();
      CreateStoryController createStoryController = new CreateStoryController(fileManager);
      CreateStoryView createStoryView = new CreateStoryView(createStoryController,
          new UniversalMenuBar(), primaryStage);
      Main.switchScene(createStoryView);
    });

    editStoryButton.setOnAction(e -> {
      StoryFileManager fileManager = new StoryFileManager();
      Story fullStory = selectStoryController.storySelected(story);
      EditStoryController editStoryController = new EditStoryController(fullStory, fileManager);
      EditStoryView editStoryView = new EditStoryView(primaryStage, editStoryController);
      Main.switchScene(editStoryView);
    });

    confirmButton.setOnAction(e -> {
      chooseStory.getSelectionModel().selectedItemProperty();
      selectStoryController.storySelected(story);
    });
  }

  /**
   * Sets up UI bindings for the choice box.
   */
  private void setupUIBindings() {
    ContextMenu contextMenu = new ContextMenu();
    MenuItem removeItem = new MenuItem("Remove");
    removeItem.setOnAction(e -> {
      String selectedStory = chooseStory.getSelectionModel().getSelectedItem();
      selectStoryController.removeStory(selectedStory);
      chooseStory.getItems().remove(selectedStory);
      chooseStory.getItems().clear(); // Clear the ChoiceBox
      selectStoryController.initialize(); // Refresh the ChoiceBox
      chooseStory.setValue(null);
    });
    contextMenu.getItems().add(removeItem);
    chooseStory.setContextMenu(contextMenu);
    chooseStory.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldValue, newValue) -> {
          if (newValue != null) {
            story = newValue;
            confirmButton.setVisible(true);
            editStoryButton.setVisible(true);
          }
        });
  }

  /**
   * Sets items in the choice box based on the provided collection.
   *
   * @param items the collection of items to be added to the choice box
   */
  public void setChoiceBoxItems(Collection<String> items) {
    chooseStory.getItems().addAll(items);
  }

  /**
   * Handles exceptions by showing an alert box with the error message.
   *
   * @param e the exception to be handled
   */
  private void handleException(Exception e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Invalid input");
    alert.setHeaderText("Validation Error:");
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }

  /**
   * Information alert
   *
   * @param msg the messeage being sent
   */
  private void informationAlert(String msg) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Max amount");
    alert.setHeaderText("Choice box can not contain more than 8 elements");
    alert.setContentText(msg);
    alert.showAndWait();
  }
}
