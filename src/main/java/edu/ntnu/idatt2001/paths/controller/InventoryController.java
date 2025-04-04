package edu.ntnu.idatt2001.paths.controller;

import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryItemEnum;
import edu.ntnu.idatt2001.paths.model.game.GameInstance;
import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.view.InventoryView;
import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

/**
 * Controller for Inventory.
 */
public class InventoryController {

  private final Player player;
  private InventoryView inventoryView;


  /**
   * Constructor for Inventory controller.
   */
  public InventoryController() {
    this.player = GameInstance.getInstance().getPlayer();
  }

  /**
   * Fills inventory gridPane with obtained inventory items.
   */
  public void fillInventory() {
    GridPane inventoryGrid = inventoryView.getInventoryGrid();
    inventoryGrid.getChildren().clear(); // Clear the grid before filling it

    int row = 0;
    int column = 0;

    for (String item : new ArrayList<>(
        player.getInventory())) { // Create a new list to avoid ConcurrentModificationException
      InventoryItemEnum inventoryItem = InventoryItemEnum.valueOf(item.toUpperCase());

      Image image = new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(inventoryItem.getImagePath())));
      ImageView imageView = new ImageView(image);
      imageView.setFitWidth(100); // Adjust this value as needed
      imageView.setFitHeight(100); // Adjust this value as needed
      imageView.setPreserveRatio(true);

      // Create a context menu for the image view
      ContextMenu contextMenu = new ContextMenu();
      MenuItem removeItem = new MenuItem("Remove");
      removeItem.setOnAction(actionEvent -> {
        player.getInventory().remove(item); // Remove item from inventory
        fillInventory(); // Refresh inventory
      });
      contextMenu.getItems().add(removeItem);

      // Show context menu when image view is right-clicked
      imageView.setOnMouseClicked(mouseEvent -> {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
          contextMenu.show(imageView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
      });

      inventoryGrid.add(imageView, column, row);

      column++;
      if (column > 1) {
        column = 0;
        row++;
      }
    }
  }

  /**
   * Sets the inventory view.
   *
   * @param inventoryView InventoryView to be set.
   */
  public void setInventoryView(InventoryView inventoryView) {
    this.inventoryView = inventoryView;
  }

}
