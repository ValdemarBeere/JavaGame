package edu.ntnu.idatt2001.paths.model.goals;

import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.GoalValidator;
import java.util.logging.Logger;

/**
 * This class represents a GoldGoal in the game. A GoldGoal is satisfied when the player accumulates
 * or exceeds a target amount of gold.
 */
public class GoldGoal implements Goal<Integer> {

  private static final Logger LOGGER = Logger.getLogger(GoldGoal.class.getName());
  private final int targetGold;

  /**
   * Constructs a new GoldGoal with the specified target amount of gold.
   *
   * @param targetGold the target amount of gold for this goal
   */
  public GoldGoal(int targetGold) {
    this.targetGold = targetGold;
    GoalValidator.validateGoals(this);
  }

  /**
   * Checks whether this GoldGoal has been fulfilled by a player.
   *
   * @param player the player to check for goal fulfillment
   * @return true if the player's gold is greater than or equal to the target gold, false otherwise
   */
  @Override
  public boolean isFulfilled(Player player) {
    boolean isFulfilled = player.getGold() >= targetGold;
    LOGGER.info("Gold goal fulfillment checked. Is Fulfilled: " + isFulfilled);
    return isFulfilled;
  }

  /**
   * Returns the target gold value of this GoldGoal.
   *
   * @return the target gold value
   */
  @Override
  public Integer getValue() {
    return targetGold;
  }
}
