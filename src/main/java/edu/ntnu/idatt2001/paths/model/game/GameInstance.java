package edu.ntnu.idatt2001.paths.model.game;

import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.player.Player;

/**
 * Instance of Game.
 */
public class GameInstance {

  private static GameInstance instance = null;

  private Player player;
  private Story story;

  private GameDifficultyEnum difficulty;

  /**
   * Constructor for GameInstance.
   */
  private GameInstance() {
  }

  /**
   * Gets GameInstance.
   *
   * @return GameInstance.
   */
  public static GameInstance getInstance() {
    if (instance == null) {
      instance = new GameInstance();
    }
    return instance;
  }

  public Player getPlayer() {
    return this.player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Story getStory() {
    return this.story;
  }

  public void setStory(Story story) {
    this.story = story;
  }

  public GameDifficultyEnum getDifficulty() {
    return this.difficulty;
  }

  public void setDifficulty(GameDifficultyEnum difficulty) {
    this.difficulty = difficulty;
  }
}
