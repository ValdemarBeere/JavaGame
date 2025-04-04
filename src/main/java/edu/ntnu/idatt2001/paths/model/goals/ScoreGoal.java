package edu.ntnu.idatt2001.paths.model.goals;

import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.GoalValidator;
import java.util.logging.Logger;

/**
 * This class represents a ScoreGoal in the game. A ScoreGoal is satisfied when the player
 * accumulates or exceeds a target amount of Score.
 */
public class ScoreGoal implements Goal<Integer> {

  private static final Logger LOGGER = Logger.getLogger(ScoreGoal.class.getName());
  private final int targetScore;

  /**
   * Constructs a new ScoreGoal with the specified target amount of Score.
   *
   * @param targetScore the target amount of Score for this goal
   */
  public ScoreGoal(int targetScore) {
    this.targetScore = targetScore;
    GoalValidator.validateGoals(this);
  }

  /**
   * Checks whether this ScoreGoal has been fulfilled by a player.
   *
   * @param player the player to check for goal fulfillment
   * @return true if the player's Score is greater than or equal to the target Score, false
   * otherwise
   */
  @Override
  public boolean isFulfilled(Player player) {
    boolean isFulfilled = player.getScore() >= targetScore;
    LOGGER.info("Health goal fulfillment checked. Is Fulfilled: " + isFulfilled);
    return isFulfilled;
  }

  /**
   * Returns the target Score value of this ScoreGoal.
   *
   * @return the target Score value
   */
  @Override
  public Integer getValue() {
    return targetScore;
  }
}
