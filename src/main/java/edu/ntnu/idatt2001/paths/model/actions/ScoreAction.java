package edu.ntnu.idatt2001.paths.model.actions;

import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.ActionValidator;
import java.util.logging.Logger;

/**
 * The type Score action.
 */
public class ScoreAction implements Action<Integer> {

  private static final Logger LOGGER = Logger.getLogger(ScoreAction.class.getName());
  private int points;

  /**
   * Instantiates a new Score action.
   *
   * @param points the points
   * @throws ActionValidationException the action validation exception
   */
  public ScoreAction(int points) {
    ActionValidator.validateAction(points);
    this.points = points;
  }

  @Override
  public void execute(final Player player) {
    player.addScore(points);
    LOGGER.info("ScoreAction executed. Score change: " + points);
  }

  @Override
  public Integer getValue() {
    return points;
  }

  @Override
  public void setValue(String newValue) {
    this.points = Integer.parseInt(newValue);
  }

  @Override
  public String getDescription() {
    String description;
    if (points < 0) {
      description = "You lost " + points + " Points";
    } else {
      description = "You received " + points + " Points";
    }
    LOGGER.info("ScoreAction description: " + description);
    return description;
  }

  @Override
  public ActionType getType() {
    return ActionType.SCOREACTION;
  }
}
