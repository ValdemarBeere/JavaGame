package edu.ntnu.idatt2001.paths.model.actions.inventory;

import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.ActionType;
import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.ActionValidator;
import java.util.logging.Logger;

/**
 * The type Inventory action.
 */
public class InventoryAction implements Action<String> {

  private static final Logger LOGGER = Logger.getLogger(InventoryAction.class.getName());
  private String item;

  /**
   * Instantiates a new Inventory action.
   *
   * @param item the item
   */
  public InventoryAction(String item) {
    ActionValidator.validateAction(item);
    this.item = item;
    LOGGER.info("InventoryAction created with item: " + item);
  }

  @Override
  public void execute(final Player player) {
    player.addToInventory(item);
    LOGGER.info("Item: " + item + " added to player's inventory.");
  }

  @Override
  public String getValue() {
    return item.toLowerCase();
  }

  @Override
  public void setValue(String newValue) {
    this.item = newValue;
  }

  @Override
  public String getDescription() {
    return "You obtained " + item + " item";
  }

  @Override
  public ActionType getType() {
    return ActionType.INVENTORYACTION;
  }
}
