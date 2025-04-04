package edu.ntnu.idatt2001.paths.model.actions;

import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.ActionValidator;
import java.util.logging.Logger;

/**
 * The type Gold action.
 */
public class GoldAction implements Action<Integer> {

  private static final Logger LOGGER = Logger.getLogger(GoldAction.class.getName());
  private int gold;

  /**
   * Instantiates a new Gold action.
   *
   * @param gold the gold
   */
  public GoldAction(int gold) {
    ActionValidator.validateAction(gold);
    this.gold = gold;
  }

  @Override
  public void execute(final Player player) {
    player.addGold(gold);
    LOGGER.info("GoldAction executed. Gold coins: " + gold);
  }

  @Override
  public Integer getValue() {
    return gold;
  }

  @Override
  public void setValue(String newValue) {
    this.gold = Integer.parseInt(newValue);
  }

  @Override
  public String getDescription() {
    String description;
    if (gold < 0) {
      description = "You lost " + gold + " coins";
    } else {
      description = "You obtained " + gold + " coins";
    }
    LOGGER.info("GoldAction description: " + description);
    return description;
  }

  @Override
  public ActionType getType() {
    return ActionType.GOLDACTION;
  }
}
