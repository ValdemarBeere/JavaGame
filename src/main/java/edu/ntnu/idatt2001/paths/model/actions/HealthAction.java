package edu.ntnu.idatt2001.paths.model.actions;

import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.ActionValidator;
import java.util.logging.Logger;

/**
 * The type Health action.
 */
public class HealthAction implements Action<Integer> {

  private static final Logger LOGGER = Logger.getLogger(HealthAction.class.getName());
  private int health;

  /**
   * Instantiates a new Health action.
   *
   * @param health the health
   */
  public HealthAction(int health) {
    ActionValidator.validateAction(health);
    this.health = health;
  }

  @Override
  public void execute(final Player player) {
    player.addHealth(health);
    LOGGER.info("HealthAction executed. Health change: " + health);
  }

  @Override
  public Integer getValue() {
    return health;
  }

  @Override
  public void setValue(String newValue) {
    this.health = Integer.parseInt(newValue);
  }

  @Override
  public String getDescription() {
    String description;
    if (health < 0) {
      description = "You lost " + health + " health";
    } else {
      description = "You gained " + health + " health";
    }
    LOGGER.info("HealthAction description: " + description);
    return description;
  }

  @Override
  public ActionType getType() {
    return ActionType.HEALTHACTION;
  }
}
