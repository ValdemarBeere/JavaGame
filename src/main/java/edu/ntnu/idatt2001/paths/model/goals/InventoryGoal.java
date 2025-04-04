package edu.ntnu.idatt2001.paths.model.goals;

import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.GoalValidator;
import java.util.logging.Logger;

/**
 * This class represents an InventoryGoal in the game. An InventoryGoal is satisfied when the player
 * obtains a certain item.
 */
public class InventoryGoal implements Goal<String> {

  private static final Logger LOGGER = Logger.getLogger(InventoryGoal.class.getName());
  private final String targetItem;

  /**
   * Constructs a new InventoryGoal with the specified target item.
   *
   * @param targetItem the target item for this goal
   */
  public InventoryGoal(String targetItem) {
    this.targetItem = targetItem;
    GoalValidator.validateGoals(this);
  }

  /**
   * Checks whether this InventoryGoal has been fulfilled by a player.
   *
   * @param player the player to check for goal fulfillment
   * @return true if the player's inventory contains the target item, false otherwise
   */
  @Override
  public boolean isFulfilled(Player player) {
    boolean isFulfilled = player.getInventory().contains(targetItem);
    LOGGER.info("Inventory goal fulfillment checked. Is Fulfilled: " + isFulfilled);
    return isFulfilled;
  }

  /**
   * Returns the target inventory item of this inventoryGoal.
   *
   * @return the target item
   */
  @Override
  public String getValue() {
    return targetItem;
  }
}

