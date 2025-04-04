package edu.ntnu.idatt2001.paths.controller;

import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.StoryFileManager;
import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.ActionType;
import edu.ntnu.idatt2001.paths.model.actions.GoldAction;
import edu.ntnu.idatt2001.paths.model.actions.HealthAction;
import edu.ntnu.idatt2001.paths.model.actions.ScoreAction;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryItemEnum;
import edu.ntnu.idatt2001.paths.model.validations.LinkValidator;
import edu.ntnu.idatt2001.paths.model.validations.PassageValidator;
import edu.ntnu.idatt2001.paths.model.validations.StoryValidator;
import edu.ntnu.idatt2001.paths.view.TextFieldConsumers;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.DefaultStringConverter;

/**
 * Controller for editing a Story. This controller is responsible for modifying the passages, links,
 * and actions within a story. Provides methods for populating and handling interactions with JavaFX
 * TableView components.
 */
public class EditStoryController {

  private final Story story;

  private final StoryFileManager fileManager;

  /**
   * Constructor for EditStoryController.
   *
   * @param story       the story
   * @param fileManager the file manager
   */
  public EditStoryController(Story story, StoryFileManager fileManager) {
    this.story = story;
    this.fileManager = fileManager;
  }

  /**
   * Create a custom cell factory for the description column of the Action TableView. It provides
   * different editing options based on the type of action (InventoryAction uses a ChoiceBox, others
   * use a TextField).
   *
   * @return A Callback object to be used as the cell factory
   */
  private static Callback<TableColumn<Action<?>, String>, TableCell<Action<?>, String>> createCustomCellFactory() {
    return param -> new TableCell<>() {
      private TextField textField;
      private ChoiceBox<InventoryItemEnum> choiceBox;

      /**
       * Updates the item in a cell.
       * @param item The item to be updated in the cell.
       * @param empty Flag indicating whether the cell is empty.
       */
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
        } else {
          Action<?> action = getTableView().getItems().get(getIndex());
          if (action instanceof InventoryAction) {
            if (choiceBox == null) {
              choiceBox = new ChoiceBox<>(
                  FXCollections.observableArrayList(InventoryItemEnum.values()));
              choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                action.setValue(newValue.getName());
                commitEdit(newValue.name());
              });
            }
            choiceBox.setValue(InventoryItemEnum.valueOf(item.toUpperCase()));
            setGraphic(choiceBox);
          } else {
            if (textField == null) {
              textField = new TextField();
              textField.setTextFormatter(
                  new TextFormatter<>(TextFieldConsumers.getIntegerInRangeFilter()));
              textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                  action.setValue(textField.getText());
                  commitEdit(textField.getText());
                }
              });
            }
            textField.setText(item);
            setGraphic(textField);
          }
        }
      }
    };
  }

  /**
   * Populate the provided TableView with given collection of passages. Set up the columns and cells
   * for editing.
   *
   * @param tableView The TableView to be populated
   * @param passages  Collection of Passage objects to populate the TableView
   */
  public void populatePassageTableView(TableView<Passage> tableView, Collection<Passage> passages) {
    tableView.setEditable(true);

    TableColumn<Passage, String> titleColumn = new TableColumn<>("Title");
    titleColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
    titleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    titleColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow())
        .setTitle(t.getNewValue()));

    TableColumn<Passage, String> contentColumn = new TableColumn<>("Content");
    contentColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getContent()));
    contentColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    contentColumn.setOnEditCommit(
        t -> t.getTableView().getItems().get(t.getTablePosition().getRow())
            .setContent(t.getNewValue()));

    tableView.getColumns().add(titleColumn);
    tableView.getColumns().add(contentColumn);

    tableView.getItems().addAll(passages);
  }

  /**
   * Populate the provided TableView with a list of links. Set up the columns and cells for
   * editing.
   *
   * @param tableView The TableView to be populated
   * @param links     List of Link objects to populate the TableView
   */
  public void populateLinkTableView(TableView<Link> tableView, List<Link> links) {
    tableView.setEditable(true);

    TableColumn<Link, String> titleColumn = new TableColumn<>("Text");
    titleColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getText()));
    titleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    titleColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow())
        .setText(t.getNewValue()));

    TableColumn<Link, String> textColumn = new TableColumn<>("Reference");
    textColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getReference()));
    textColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    textColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow())
        .setReference(t.getNewValue()));

    TableColumn<Link, InventoryItemEnum> itemRequirementColumn = new TableColumn<>(
        "Item Requirement");
    itemRequirementColumn.setOnEditCommit(t -> {
      InventoryAction newInventoryAction = new InventoryAction(t.getNewValue().getName());
      t.getTableView().getItems().get(t.getTablePosition().getRow())
          .setRequiredItem(newInventoryAction);
    });
    itemRequirementColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(
        FXCollections.observableArrayList(InventoryItemEnum.values())));

    tableView.getColumns().add(titleColumn);
    tableView.getColumns().add(textColumn);
    tableView.getColumns().add(itemRequirementColumn);

    tableView.getItems().addAll(links);
  }

  /**
   * Populate the provided TableView with a list of actions. Set up the columns and cells for
   * editing.
   *
   * @param tableView The TableView to be populated
   * @param actions   List of Action objects to populate the TableView
   */
  public void populateActionTableView(TableView<Action<?>> tableView, List<Action<?>> actions) {
    tableView.setEditable(true);

    TableColumn<Action<?>, ActionType> typeColumn = new TableColumn<>("Type");
    typeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
        cellData.getValue().getType()
    ));
    typeColumn.setCellFactory(
        ChoiceBoxTableCell.forTableColumn(FXCollections.observableArrayList(ActionType.values())));
    typeColumn.setOnEditCommit(t -> {
      ActionType newType = t.getNewValue();
      Action<?> oldAction = t.getRowValue();
      Action<?> newAction = switch (newType) {
        case GOLDACTION -> new GoldAction((Integer) oldAction.getValue());
        case HEALTHACTION -> new HealthAction((Integer) oldAction.getValue());
        case SCOREACTION -> new ScoreAction((Integer) oldAction.getValue());
        case INVENTORYACTION -> new InventoryAction((String) oldAction.getValue());
      };
      t.getTableView().getItems().set(t.getTablePosition().getRow(), newAction);
    });

    TableColumn<Action<?>, String> descriptionColumn = new TableColumn<>("Description");
    descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        String.valueOf(cellData.getValue().getValue())
    ));
    descriptionColumn.setCellFactory(createCustomCellFactory());

    descriptionColumn.setOnEditCommit(
        t -> t.getTableView().getItems().get(t.getTablePosition().getRow())
            .setValue(t.getNewValue()));

    tableView.getColumns().add(typeColumn);
    tableView.getColumns().add(descriptionColumn);

    tableView.getItems().addAll(actions);
  }

  /**
   * Get a list of Link objects from the selected Passage.
   *
   * @param selectedPassage The Passage object from which to retrieve Link objects
   * @return List of Link objects from the selected Passage
   */
  public List<Link> getLinksFromSelectedPassage(Passage selectedPassage) {
    return selectedPassage.getLinks();
  }

  /**
   * Get a list of Action objects from the selected Link.
   *
   * @param selectedLink The Link object from which to retrieve Action objects
   * @return List of Action objects from the selected Link
   */
  public List<Action<?>> getActionsFromSelectedLink(Link selectedLink) {
    return selectedLink.getActions();
  }

  /**
   * Add row to passage table.
   *
   * @param tableView the table view
   */
  public void addRowToPassageTable(TableView<Passage> tableView) {
    Passage newPassage = new Passage("Enter title here", "Enter content");
    story.addPassage(newPassage);
    tableView.getItems().add(newPassage);
  }

  /**
   * Removes a passage from the story.
   *
   * @param tableView the table view of passages
   * @param passage   the passage to remove
   */
  public void removePassage(TableView<Passage> tableView, Passage passage) {
    if (passage == null) {
      return;
    }
    // Remove all links that reference the passage to be removed
    for (Passage p : story.getPassages()) {
      p.getLinks().removeIf(link -> link.getReference().equals(passage.getTitle()));
    }
    // Remove the passage from the story
    story.removePassageUsingPassage(passage);
    // Remove the passage from the table view
    tableView.getItems().remove(passage);
  }


  /**
   * Add row to link table.
   *
   * @param tableView       the table view
   * @param selectedPassage the selected passage
   */
  public void addRowToLinkTable(TableView<Link> tableView, Passage selectedPassage) {
    Link newLink = new Link("Enter your link text here", "Enter the reference here");
    selectedPassage.addLink(newLink);
    tableView.getItems().add(newLink);
  }

  /**
   * Remove row from link table.
   *
   * @param tableView       the table view
   * @param selectedPassage the selected passage
   */
  public void removeRowFromLinkTable(TableView<Link> tableView, Passage selectedPassage) {
    Link selectedLink = tableView.getSelectionModel().getSelectedItem();
    selectedPassage.removeLink(selectedLink);
    tableView.getItems().remove(selectedLink);
  }

  /**
   * Add row to action table.
   *
   * @param tableView the table view
   */
  public void addRowToActionTable(TableView<Action<?>> tableView) {
    Dialog<Pair<ActionType, String>> dialog = new Dialog<>();
    dialog.setTitle("Add Row");

    ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

    ComboBox<ActionType> comboBox = new ComboBox<>();
    comboBox.getItems().addAll(ActionType.values());

    TextField textField = new TextField();
    textField.setPromptText("Value");
    textField.setTextFormatter(new TextFormatter<>(TextFieldConsumers.getIntegerInRangeFilter()));

    ChoiceBox<InventoryItemEnum> choiceBox = new ChoiceBox<>();
    choiceBox.getItems().addAll(InventoryItemEnum.values());
    choiceBox.setVisible(false);

    comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == ActionType.INVENTORYACTION) {
        choiceBox.setVisible(true);
        textField.setVisible(false);
      } else {
        choiceBox.setVisible(false);
        textField.setVisible(true);
      }
    });

    GridPane grid = new GridPane();
    grid.add(new Label("ActionType:"), 0, 0);
    grid.add(comboBox, 1, 0);
    grid.add(new Label("Value:"), 0, 1);
    grid.add(textField, 1, 1);
    grid.add(choiceBox, 1, 1);

    dialog.getDialogPane().setContent(grid);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == addButtonType) {
        if (comboBox.getValue() == ActionType.INVENTORYACTION) {
          return new Pair<>(comboBox.getValue(), choiceBox.getValue().getName());
        } else {
          return new Pair<>(comboBox.getValue(), textField.getText());
        }
      }
      return null;
    });

    Optional<Pair<ActionType, String>> result = dialog.showAndWait();

    result.ifPresent(pair -> {
      ActionType type = pair.getKey();
      String value = pair.getValue();
      Action<?> newAction = switch (type) {
        case GOLDACTION -> new GoldAction(Integer.parseInt(value));
        case HEALTHACTION -> new HealthAction(Integer.parseInt(value));
        case SCOREACTION -> new ScoreAction(Integer.parseInt(value));
        case INVENTORYACTION -> new InventoryAction(value);
      };
      tableView.getItems().add(newAction);
    });
  }


  /**
   * Remove row from action table.
   *
   * @param tableView the table view
   */
  public void removeRowFromActionTable(TableView<Action<?>> tableView) {
    tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem());
  }

  /**
   * Save edited story to file.
   *
   * @param editedStory the edited story
   */
  public void saveEditedStoryToFile(Story editedStory) {
    try {
      Path directoryPath = Paths.get("userStories");
      Path filePath = directoryPath.resolve(story.getTitle() + ".paths");

      if (!checkFileExists(filePath.toString())) {
        // File exists and user chose not to replace it, return without saving
        return;
      }
      Files.createDirectories(filePath.getParent()); // Ensure the directory exists
      if (editedStory.getOpeningPassage() == null) {
        Passage newOpeningPassage = editedStory.getFirstPassage();
        if (newOpeningPassage != null) {
          editedStory = new Story(editedStory.getTitle(), editedStory.getPassagesMap(),
              newOpeningPassage);
        }

      }
      //write the edited story to the file
      fileManager.saveStoryToFile(editedStory, directoryPath.toString());
      StoryValidator.validateStory(editedStory.getTitle(), editedStory.getPassagesMap(),
          editedStory.getOpeningPassage());
      for (Passage passage : editedStory.getPassages()) {
        PassageValidator.validatePassage(passage.getTitle(),
            passage.getContent(), passage.getLinks());
        for (Link link : passage.getLinks()) {
          LinkValidator.validateLink(link.getText(), link.getReference(),
              link.getActions());
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean checkFileExists(String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("File Exists");
      alert.setHeaderText("A file with the same name already exists.");
      alert.setContentText("Do you want to replace the existing file?");

      Optional<ButtonType> result = alert.showAndWait();
      // User chose not to replace the file, return false
      if (result.isPresent()) {
        return result.get() == ButtonType.OK;
      }
    }
    // File does not exist or user chose to replace it, return true
    return true;
  }

  /**
   * Gets story.
   *
   * @return the story
   */
  public Story getStory() {
    return this.story;
  }

}
