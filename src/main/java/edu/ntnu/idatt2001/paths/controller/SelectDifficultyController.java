package edu.ntnu.idatt2001.paths.controller;

import edu.ntnu.idatt2001.paths.model.game.GameDifficultyEnum;
import edu.ntnu.idatt2001.paths.model.game.GameInstance;
import java.util.function.Consumer;

/**
 * Controller for SelectDifficulty.
 */
public class SelectDifficultyController {

  private Consumer<GameDifficultyEnum> gameStarter;

  /**
   * Constructor for SelectDifficultyController.
   */
  public SelectDifficultyController() {
  }

  /**
   * Set selected difficulty.
   *
   * @param difficulty selected difficulty.
   */
  public void difficultySelected(GameDifficultyEnum difficulty) {
    GameInstance.getInstance().setDifficulty(difficulty);
    if (gameStarter != null) {
      gameStarter.accept(difficulty);
    }
  }

  /**
   * Sets Consumer<GameDifficultyEnum>.
   *
   * @param gameStarter Consumer<GameDifficultyEnum>.
   */
  public void setGameStarter(Consumer<GameDifficultyEnum> gameStarter) {
    this.gameStarter = gameStarter;
  }
}
