package edu.ntnu.idatt2001.paths.model.goals;

import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.GoalValidator;
import java.util.logging.Logger;

/**
 * This class represents a HealthGoal in the game. A HealthGoal is satisfied when the player
 * accumulates or exceeds a target amount of Health.
 */
public class HealthGoal implements Goal<Integer> {

  private static final Logger LOGGER = Logger.getLogger(HealthGoal.class.getName());
  private final int targetHealth;

  /**
   * Constructs a new HealthGoal with the specified target amount of Health.
   *
   * @param targetHealth the target amount of Health for this goal
   */
  public HealthGoal(int targetHealth) {
    this.targetHealth = targetHealth;
    GoalValidator.validateGoals(this);
  }

  /**
   * Checks whether this HealthGoal has been fulfilled by a player.
   *
   * @param player the player to check for goal fulfillment
   * @return true if the player's Health is greater than or equal to the target Health, false
   * otherwise
   */
  @Override
  public boolean isFulfilled(Player player) {
    boolean isFulfilled = player.getHealth() >= targetHealth;
    LOGGER.info("Health goal fulfillment checked. Is Fulfilled: " + isFulfilled);
    return isFulfilled;
  }

  /**
   * Returns the target Health value of this HealthGoal.
   *
   * @return the target Health value
   */
  @Override
  public Integer getValue() {
    return targetHealth;
  }
}
