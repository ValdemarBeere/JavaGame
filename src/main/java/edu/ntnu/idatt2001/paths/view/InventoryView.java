package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.controller.GameController;
import edu.ntnu.idatt2001.paths.model.game.GameInstance;
import java.util.Objects;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * View class for Inventory.
 */
public class InventoryView extends VBox {

  private final GridPane inventoryGrid;

  /**
   * Constructor for the GameView class. Initializes the components of the UI and arranges them on
   * the stage.
   *
   * @param gameController the controller class of game.
   */
  public InventoryView(GameController gameController) {

    getChildren().add(new UniversalMenuBar());
    setPrefSize(1600, 900);
    BackgroundImage backgroundImage = new BackgroundImage(
        gameController.getBackgroundImageView().getImage(),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
        new BackgroundSize(1.0, 1.0, true, true, false, true)
    );

    // Create a new Background with the new BackgroundImage
    Background newBackground = new Background(backgroundImage);
    // Set the new Background to the root
    setBackground(newBackground);

    GridPane mainGrid = new GridPane();
    mainGrid.setPrefSize(1600, 900);
    RowConstraints Row1 = new RowConstraints();
    Row1.setPrefHeight(800);
    RowConstraints Row2 = new RowConstraints();
    Row2.setPrefHeight(100);
    ColumnConstraints Column1 = new ColumnConstraints();
    Column1.setPercentWidth(100);
    mainGrid.getColumnConstraints().add(Column1);
    mainGrid.getRowConstraints().addAll(Row1, Row2);
    getChildren().add(mainGrid);

    GridPane nestedGrid = new GridPane();
    ColumnConstraints nestedColumn1 = new ColumnConstraints();
    nestedColumn1.setPercentWidth(50);
    ColumnConstraints nestedColumn2 = new ColumnConstraints();
    nestedColumn2.setPercentWidth(50);
    RowConstraints nestedRow1 = new RowConstraints();
    nestedRow1.setPercentHeight(100);
    nestedGrid.getColumnConstraints().addAll(nestedColumn1, nestedColumn2);
    nestedGrid.getRowConstraints().add(nestedRow1);
    GridPane.setConstraints(nestedGrid, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    mainGrid.getChildren().add(nestedGrid);

    inventoryGrid = new GridPane();
    ColumnConstraints inventoryColumn1 = new ColumnConstraints();
    inventoryColumn1.setPercentWidth(50);
    ColumnConstraints inventoryColumn2 = new ColumnConstraints();
    inventoryColumn2.setPercentWidth(50);
    RowConstraints inventoryRow1 = new RowConstraints();
    inventoryRow1.setPercentHeight(12.5);
    RowConstraints inventoryRow2 = new RowConstraints();
    inventoryRow2.setPercentHeight(12.5);
    RowConstraints inventoryRow3 = new RowConstraints();
    inventoryRow3.setPercentHeight(12.5);
    RowConstraints inventoryRow4 = new RowConstraints();
    inventoryRow4.setPercentHeight(12.5);
    RowConstraints inventoryRow5 = new RowConstraints();
    inventoryRow5.setPercentHeight(12.5);
    RowConstraints inventoryRow6 = new RowConstraints();
    inventoryRow6.setPercentHeight(12.5);
    RowConstraints inventoryRow7 = new RowConstraints();
    inventoryRow7.setPercentHeight(12.5);
    RowConstraints inventoryRow8 = new RowConstraints();
    inventoryRow8.setPercentHeight(12.5);
    inventoryGrid.getColumnConstraints().addAll(inventoryColumn1, inventoryColumn2);
    inventoryGrid.getRowConstraints()
        .addAll(inventoryRow1, inventoryRow2, inventoryRow3, inventoryRow4, inventoryRow5,
            inventoryRow6, inventoryRow7, inventoryRow8);
    GridPane.setConstraints(inventoryGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    nestedGrid.getChildren().add(inventoryGrid);

    Button closeButton = new Button("Close");
    closeButton.getStyleClass().addAll("transparentButton", "continueText");
    closeButton.getStylesheets().addAll(
        Objects.requireNonNull(VictoryView.class.getResource("/styles/buttons.css"))
            .toExternalForm(),
        Objects.requireNonNull(VictoryView.class.getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(closeButton, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    closeButton.setPadding(new Insets(1, 1, 1, 1));
    GridPane.setMargin(closeButton, new Insets(0, 0, 50, 0));
    closeButton.setOnAction(e -> Main.switchToPreviousScene());

    Image playerImage = GameInstance.getInstance().getPlayer().getImage();
    ImageView playerImageView = new ImageView();
    playerImageView.setImage(playerImage);
    playerImageView.setPreserveRatio(true);
    playerImageView.setFitWidth(800);
    playerImageView.setEffect(new DropShadow(10, Color.WHITE));
    GridPane.setConstraints(playerImageView, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    nestedGrid.getChildren().add(playerImageView);

    mainGrid.getChildren().add(closeButton);
  }

  public GridPane getInventoryGrid() {
    return inventoryGrid;
  }
}
