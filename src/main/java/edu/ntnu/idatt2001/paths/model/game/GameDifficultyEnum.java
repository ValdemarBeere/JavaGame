package edu.ntnu.idatt2001.paths.model.game;


import edu.ntnu.idatt2001.paths.model.player.PlayerStatEnum;

/**
 * The enum Game difficulty enum. This enum represents the different difficulties of the game. The
 * enum is used to set the default values for the player stats.
 */
public enum GameDifficultyEnum {
  /**
   * The default difficulty.
   */
  DEFAULT,
  /**
   * The easy difficulty.
   */
  EASY,
  /**
   * The medium difficulty.
   */
  MEDIUM,
  /**
   * The hard difficulty.
   */
  HARD;

  /**
   * Gets the default health value for the difficulty.
   *
   * @return the default health value.
   */
  public int getDefaultHealth() {
    return switch (this) {
      case DEFAULT -> PlayerStatEnum.DEFAULT_HEALTH.getValue();
      case EASY -> PlayerStatEnum.EASY_HEALTH.getValue();
      case MEDIUM -> PlayerStatEnum.MEDIUM_HEALTH.getValue();
      case HARD -> PlayerStatEnum.HARD_HEALTH.getValue();
    };
  }

  /**
   * Gets the default score value for the difficulty.
   *
   * @return the default score value.
   */
  public int getDefaultScore() {
    return switch (this) {
      case DEFAULT -> PlayerStatEnum.DEFAULT_SCORE.getValue();
      case EASY -> PlayerStatEnum.EASY_SCORE.getValue();
      case MEDIUM -> PlayerStatEnum.MEDIUM_SCORE.getValue();
      case HARD -> PlayerStatEnum.HARD_SCORE.getValue();
    };
  }

  /**
   * Gets the default gold value for the difficulty.
   *
   * @return the default gold value.
   */
  public int getDefaultGold() {
    return switch (this) {
      case DEFAULT -> PlayerStatEnum.DEFAULT_GOLD.getValue();
      case EASY -> PlayerStatEnum.EASY_GOLD.getValue();
      case MEDIUM -> PlayerStatEnum.MEDIUM_GOLD.getValue();
      case HARD -> PlayerStatEnum.HARD_GOLD.getValue();
    };
  }
}
