package edu.ntnu.idatt2001.paths.model.player;

/**
 * The enum Player stat enum. This enum represents the different stats a player can have based on
 * the different difficulties of the game. The enum also contains the default values for each stat.
 */
public enum PlayerStatEnum {
  /**
   * The default health value.
   */
  DEFAULT_HEALTH(100),
  /**
   * The default score value.
   */
  DEFAULT_SCORE(0),
  /**
   * The default gold value.
   */
  DEFAULT_GOLD(0),
  /**
   * The easy health value.
   */
  EASY_HEALTH(150),
  /**
   * The easy score value.
   */
  EASY_SCORE(10),
  /**
   * The easy gold value.
   */
  EASY_GOLD(10),
  /**
   * The medium health value.
   */
  MEDIUM_HEALTH(100),
  /**
   * The medium score value.
   */
  MEDIUM_SCORE(0),
  /**
   * The medium gold value.
   */
  MEDIUM_GOLD(0),
  /**
   * The hard health value.
   */
  HARD_HEALTH(50),
  /**
   * The hard score value.
   */
  HARD_SCORE(0),
  /**
   * The hard gold value.
   */
  HARD_GOLD(0);

  private final int value;

  /**
   * Instantiates a new Player stat enum.
   *
   * @param value the value
   */
  PlayerStatEnum(int value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public int getValue() {
    return value;
  }
}
