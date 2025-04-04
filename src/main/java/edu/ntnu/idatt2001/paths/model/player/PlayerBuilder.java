package edu.ntnu.idatt2001.paths.model.player;

import edu.ntnu.idatt2001.paths.model.game.GameDifficultyEnum;
import java.util.List;
import javafx.scene.image.Image;

/**
 * The class Player builder. This class is used to build a player object with the builder pattern.
 */
public class PlayerBuilder {

  private String name;
  private int health;
  private int score;
  private int gold;
  private List<String> inventory;

  private Image image;

  /**
   * Instantiates a new Player builder. Sets the default values for the player.
   *
   * @param difficulty the difficulty of the game.
   */
  public PlayerBuilder(GameDifficultyEnum difficulty) {
    this.name = "Player";
    this.health = difficulty.getDefaultHealth();
    this.score = difficulty.getDefaultScore();
    this.gold = difficulty.getDefaultGold();
    this.inventory = List.of();
    this.image = null;
  }

  /**
   * Instantiates a new Player builder. Sets the default values for the player.
   */
  public PlayerBuilder() {
    this(GameDifficultyEnum.DEFAULT);
  }


  /**
   * Method to build a player object.
   *
   * @return the player object.
   */
  public Player build() {
    return new Player(this);
  }

  /**
   * Method to build a player object.
   *
   * @return the player object.
   */
  public String getName() {
    return name;
  }

  /**
   * Method to set the name of the player.
   *
   * @param name the name of the player.
   * @return the player builder object.
   */
  public PlayerBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Method to get the health of the player.
   *
   * @return the health of the player.
   */
  public int getHealth() {
    return health;
  }

  /**
   * Method to set the health of the player.
   *
   * @param health the health of the player.
   * @return the player builder object.
   */
  public PlayerBuilder setHealth(int health) {
    this.health = health;
    return this;
  }

  /**
   * Method to get the score of the player.
   *
   * @return the score of the player.
   */
  public int getScore() {
    return score;
  }

  /**
   * Method to set the score of the player.
   *
   * @param score the score of the player.
   * @return the player builder object.
   */
  public PlayerBuilder setScore(int score) {
    this.score = score;
    return this;
  }

  /**
   * Method to get the gold of the player.
   *
   * @return the gold of the player.
   */
  public int getGold() {
    return gold;
  }

  /**
   * Method to set the gold of the player.
   *
   * @param gold the gold of the player.
   * @return the player builder object.
   */
  public PlayerBuilder setGold(int gold) {
    this.gold = gold;
    return this;
  }

  /**
   * Method to get the inventory of the player.
   *
   * @return the inventory of the player.
   */
  public List<String> getInventory() {
    return inventory;
  }

  /**
   * Method to set the inventory of the player.
   *
   * @param items the items of the player.
   * @return the player builder object.
   */
  public PlayerBuilder setInventory(List<String> items) {
    inventory = items;
    return this;
  }

  /**
   * Method to get the image of a player
   *
   * @return the inventory of the player
   */
  public Image getImage() {
    return image;
  }

  /**
   * Method to set the image of the player
   *
   * @param image image to the player
   * @return the image of the player
   */
  public PlayerBuilder setImage(Image image) {
    this.image = image;
    return this;
  }

}

