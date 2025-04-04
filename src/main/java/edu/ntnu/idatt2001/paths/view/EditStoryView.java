package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.controller.EditStoryController;
import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.LinkValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import java.util.Objects;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * View class for EditStory.
 */
public class EditStoryView extends VBox {

  private final EditStoryController controller;

  /**
   * Constructor for the EditStoryView class.
   *
   * @param primaryStage        the primary stage on which the UI components are arranged
   * @param editStoryController the controller for this view.
   */
  public EditStoryView(Stage primaryStage, EditStoryController editStoryController) {
    controller = editStoryController;
    getChildren().add(new UniversalMenuBar());

    setPrefSize(1600, 900);
    getStylesheets().add(
        Objects.requireNonNull(VictoryView.class.getResource("/styles/background.css"))
            .toExternalForm());
    getStyleClass().add("background");

    // Create the main GridPane with 2 columns and 1 row.
    GridPane mainGrid = new GridPane();
    mainGrid.getColumnConstraints()
        .addAll(createColumnConstraints(50), createColumnConstraints(50));
    mainGrid.getRowConstraints().addAll(createRowConstraints(90),
        createRowConstraints(10)); // 90% for the table views, 10% for the button
    getChildren().add(mainGrid);

// Create a GridPane for the left side, with 1 column and 1 row.
    GridPane leftGrid = new GridPane();
    leftGrid.getColumnConstraints().add(createColumnConstraints(100));
    leftGrid.getRowConstraints().add(createRowConstraints(100));
    GridPane.setConstraints(leftGrid, 0, 0);
    mainGrid.getChildren().add(leftGrid);

// Create a GridPane for the right side, with 1 column and 2 rows.
    GridPane rightGrid = new GridPane();
    rightGrid.getColumnConstraints().add(createColumnConstraints(100));
    rightGrid.getRowConstraints().addAll(createRowConstraints(50), createRowConstraints(50));
    GridPane.setConstraints(rightGrid, 1, 0);
    mainGrid.getChildren().add(rightGrid);

    Button confirmButton = new Button("Confirm Story");
    confirmButton.getStyleClass().addAll("transparentButton", "continueText");
    confirmButton.getStylesheets().addAll(
        Objects.requireNonNull(VictoryView.class.getResource("/styles/buttons.css"))
            .toExternalForm(),
        Objects.requireNonNull(VictoryView.class.getResource("/styles/text.css")).toExternalForm());
    confirmButton.setPadding(new Insets(1, 1, 1, 1));
    SelectStoryView selectStoryView = new SelectStoryView(primaryStage);
    confirmButton.setOnAction(e -> {
      try {
        controller.saveEditedStoryToFile(controller.getStory());
      } catch (StoryValidationException | PassageValidationException |
               ActionValidationException | LinkValidationException ex) {
        handleException(ex);
        return;
      }

      Main.switchScene(selectStoryView);
    });
    GridPane.setConstraints(confirmButton, 0, 1, 2, 1, HPos.CENTER,
        VPos.CENTER); // Span it across both columns at the 2nd row
    mainGrid.getChildren().addAll(confirmButton);

    // Add the TableViews to the GridPanes.
    TableView<Passage> passageView = new TableView<>();
    passageView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    GridPane.setConstraints(passageView, 0, 0);
    leftGrid.getChildren().add(passageView);
    setContextMenuForPassageTable(passageView);

    TableView<Link> linkView = new TableView<>();
    GridPane.setConstraints(linkView, 0, 0);
    rightGrid.getChildren().add(linkView);
    linkView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    setContextMenuForLinkTable(linkView, passageView);

    TableView<Action<?>> actionView = new TableView<>();
    GridPane.setConstraints(actionView, 0, 1);
    rightGrid.getChildren().add(actionView);
    actionView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    setContextMenuForActionTable(actionView);

    controller.populatePassageTableView(passageView, controller.getStory().getPassages());

    passageView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldValue, newValue) -> {
          if (newValue != null) {
            linkView.getColumns().clear();
            linkView.getItems().clear(); // clear the table before populating
            controller.populateLinkTableView(linkView,
                controller.getLinksFromSelectedPassage(newValue));
          }
        });

    linkView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldValue, newValue) -> {
          if (newValue != null) {
            actionView.getColumns().clear();
            actionView.getItems().clear(); // clear the table before populating
            controller.populateActionTableView(actionView,
                controller.getActionsFromSelectedLink(newValue));
          }
        });

// Make TableView resize
    passageView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    linkView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    actionView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
  }

  private ColumnConstraints createColumnConstraints(double width) {
    ColumnConstraints column = new ColumnConstraints();
    column.setPercentWidth(width);
    return column;
  }

  private RowConstraints createRowConstraints(double height) {
    RowConstraints row = new RowConstraints();
    row.setPercentHeight(height);
    return row;
  }

  private void setContextMenuForPassageTable(TableView<Passage> tableView) {
    ContextMenu contextMenu = new ContextMenu();

    MenuItem addRowMenuItem = new MenuItem("Add row");
    addRowMenuItem.setOnAction(e -> controller.addRowToPassageTable(tableView));
    contextMenu.getItems().add(addRowMenuItem);

    MenuItem removeRowMenuItem = new MenuItem("Remove row");
    removeRowMenuItem.setOnAction(e -> {
      Passage selectedPassage = tableView.getSelectionModel().getSelectedItem();
      controller.removePassage(tableView, selectedPassage);
    });
    contextMenu.getItems().add(removeRowMenuItem);

    tableView.setContextMenu(contextMenu);
  }

  private void setContextMenuForLinkTable(TableView<Link> tableView,
      TableView<Passage> passageTableView) {
    ContextMenu contextMenu = new ContextMenu();

    MenuItem addRowMenuItem = new MenuItem("Add row");
    addRowMenuItem.setOnAction(e -> controller.addRowToLinkTable(tableView,
        passageTableView.getSelectionModel().getSelectedItem()));
    contextMenu.getItems().add(addRowMenuItem);

    MenuItem removeRowMenuItem = new MenuItem("Remove row");
    removeRowMenuItem.setOnAction(e -> controller.removeRowFromLinkTable(tableView,
        passageTableView.getSelectionModel().getSelectedItem()));
    contextMenu.getItems().add(removeRowMenuItem);

    tableView.setContextMenu(contextMenu);
  }

  private void setContextMenuForActionTable(TableView<Action<?>> tableView) {
    ContextMenu contextMenu = new ContextMenu();

    MenuItem addRowMenuItem = new MenuItem("Add row");
    addRowMenuItem.setOnAction(e -> controller.addRowToActionTable(tableView));
    contextMenu.getItems().add(addRowMenuItem);

    MenuItem removeRowMenuItem = new MenuItem("Remove row");
    removeRowMenuItem.setOnAction(e -> controller.removeRowFromActionTable(tableView));
    contextMenu.getItems().add(removeRowMenuItem);

    tableView.setContextMenu(contextMenu);
  }

  private void handleException(Exception e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Invalid input");
    alert.setHeaderText("Validation Error:");
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }
}
