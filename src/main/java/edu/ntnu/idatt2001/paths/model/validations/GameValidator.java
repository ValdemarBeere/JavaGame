package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.exceptions.GameValidationException;
import edu.ntnu.idatt2001.paths.model.goals.Goal;
import edu.ntnu.idatt2001.paths.model.player.Player;
import java.util.List;

public class GameValidator {

  public static void validateGame(Player player, Story story, List<Goal<?>> goals) {
    if (story == null) {
      throw new GameValidationException("Story can not be null");
    }
    if (player == null) {
      throw new GameValidationException("Player can not be null");
    }
    if (goals == null) {
      throw new GameValidationException("Goals can not be null");
    }
    if (goals.isEmpty()) {
      throw new GameValidationException("goals can not be empty");
    }
    for (Goal<?> goal : goals) {
      if (goal == null) {
        throw new GameValidationException("A Goal in goals can not be null");
      }
    }
  }
}
