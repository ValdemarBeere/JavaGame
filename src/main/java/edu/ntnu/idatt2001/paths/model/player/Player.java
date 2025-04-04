package edu.ntnu.idatt2001.paths.model.player;

import edu.ntnu.idatt2001.paths.model.exceptions.PlayerValidationException;
import edu.ntnu.idatt2001.paths.model.validations.PlayerValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 * The Player class. Represents who is playing the game and what they own. The player has a name,
 * health, score, gold and inventory. When a player has 0 or less health, they are dead.
 */
public class Player {

  private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
  private static final int MAX_INVENTORY_SIZE = 16;
  private final String name;
  private final List<String> inventory;
  private int health;
  private int score;
  private int gold;
  private Image image;

  /**
   * The builder constructor for the Player class.
   *
   * @param pb PlayerBuilder - the builder object
   * @throws NullPointerException     thrown if name or inventory is null or contains null values
   * @throws IllegalArgumentException thrown if name is blank, health is zero or lower, gold is
   *                                  negative
   */
  public Player(PlayerBuilder pb) throws NullPointerException, IllegalArgumentException {
    this(pb.getName(), pb.getHealth(), pb.getScore(), pb.getGold(), pb.getInventory(),
        pb.getImage());
  }

  /**
   * The main constructor for the Player class. Name can not be blank or null. Health, and gold can
   * not be negative. A player can have zero health, but can not start with zero. Inventory can not
   * be null. Loops through the inventory and converts all items to trimmed strings in lowercase.
   *
   * @param name      String - name of the player
   * @param health    int - positive number representing the player's starting health
   * @param score     int - number representing the player's starting score
   * @param gold      int - positive number representing the player's amount of starting gold
   * @param inventory List - list of starting items the player has represented by strings
   * @throws NullPointerException     thrown if name or inventory is null or contains null values
   * @throws IllegalArgumentException thrown if name is blank, health is zero or lower, gold is
   *                                  negative
   */
  public Player(String name, int health, int score, int gold, List<String> inventory, Image image) {

    PlayerValidator.validatePlayer(name, health, score, gold, inventory);
    (this.inventory = new ArrayList<>()).addAll(
        inventory.stream().map(String::trim).map(String::toLowerCase).toList()
    );
    this.name = name;
    this.health = health;
    this.score = score;
    this.gold = gold;
    this.image = image;
  }

  /**
   * Creates a new player through the main constructor. Inventory is set to an empty list.
   *
   * @param name   String - name of the player
   * @param health int - positive number representing the player's starting health
   * @param score  int - number representing the player's starting score
   * @param gold   int - positive number representing the player's amount of starting gold
   * @throws PlayerValidationException thrown if name is blank, or if Image is Null health is zero
   *                                   or lower or if gold is negative or if name is null
   */
  public Player(String name, int health, int score, int gold, Image image) {
    this(name, health, score, gold, new ArrayList<>(), image);
  }

  /**
   * Creates a new player with the given name, health, score, and gold. Inventory and image are set
   * to default values.
   *
   * @param name   String - name of the player
   * @param health int - positive number representing the player's starting health
   * @param score  int - number representing the player's starting score
   * @param gold   int - positive number representing the player's amount of starting gold
   * @throws NullPointerException     thrown if name is null
   * @throws IllegalArgumentException thrown if name is blank, health is zero or lower or if gold is
   *                                  negative
   */
  public Player(String name, int health, int score, int gold)
      throws NullPointerException, IllegalArgumentException {
    this(name, health, score, gold, new ArrayList<>(), null);
  }


  /**
   * Creates a new player with only name. Health, score, gold and inventory are set to default
   * values which are set as static constants in the class.
   *
   * @param name String - name of the player
   * @throws NullPointerException     thrown if name is null
   * @throws IllegalArgumentException thrown if name is blank
   */
  public Player(String name) {
    this(
        name,
        PlayerStatEnum.DEFAULT_HEALTH.getValue(),
        PlayerStatEnum.DEFAULT_SCORE.getValue(),
        PlayerStatEnum.DEFAULT_GOLD.getValue(),
        new ArrayList<>(),
        null
    );
  }

  /**
   * Retrieves the name of the player
   *
   * @return String - name of the player
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieves the player's health
   *
   * @return int - health of the player
   */
  public int getHealth() {
    return health;
  }

  /**
   * Checks if the player has health above 0
   *
   * @return boolean - true if player is alive, false if player is dead
   */
  public boolean isAlive() {
    return health > 0;
  }

  /**
   * Adds a number to the player's health. This number can be positive or negative. If health
   * becomes negative, it is set to 0.
   *
   * @param health int - positive or negative number to add to the player's health
   */
  public void addHealth(int health) {
    this.health += health;
    if (this.health < 0) {
      this.health = 0;
    }
    LOGGER.info("Health adjusted. Current health: " + this.health);
  }

  /**
   * Checks if inventory size is bigger than 16
   *
   * @return true >=16 false if not
   */
  public boolean isInventoryFull() {
    return inventory.size() >= MAX_INVENTORY_SIZE;
  }

  /**
   * Retrieves the player's score
   *
   * @return int - score of the player
   */
  public int getScore() {
    return score;
  }

  /**
   * Adds a number to the player's score. This number can be positive or negative.
   *
   * @param score int - positive or negative number to add to the player's score
   */
  public void addScore(int score) {
    this.score += score;
    LOGGER.info("Score adjusted. Current score: " + this.score);
  }

  /**
   * Retrieves the gold of the player
   *
   * @return String - gold of the player
   */
  public int getGold() {
    return gold;
  }

  /**
   * Adds a number to the player's gold count. This number can be positive or negative. If the
   * amount of becomes negative, it is set to 0.
   *
   * @param gold int - positive or negative number to add to the player's gold
   */
  public void addGold(int gold) {
    this.gold += gold;
    if (this.gold < 0) {
      this.gold = 0;
    }
    LOGGER.info("Gold adjusted. Current gold: " + this.gold);
  }

  /**
   * Retrieves the inventory of the player by aggregation
   *
   * @return List - list of items the player has
   */
  public List<String> getInventory() {
    return inventory;
  }

  /**
   * Adds an element to the player's inventory Trims and converts to lowercase before adding as
   * inventory is stored in lowercase
   *
   * @param item String - an item to add to the inventory
   */
  public void addToInventory(String item) {
    PlayerValidator.validateAddToInventory(item);
    inventory.add(item.trim().toLowerCase());
    LOGGER.info("Item added to inventory: " + item.trim().toLowerCase());
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
   * Method to set the image of a player
   *
   * @param image the new image
   */
  public void setImage(Image image) {
    this.image = image;
  }


  /**
   * Checks to see if a specified item is in the player's inventory Possible to send null as
   * parameter, in which case false is returned item is trimmed and converted to lowercase before
   * checking as inventory is stored in lowercase
   *
   * @param item String - the item to check for
   * @return boolean - true if the item is in the inventory, false if not
   * @throws NullPointerException     thrown if the item is null
   * @throws IllegalArgumentException thrown if item is blank
   */
  public boolean isInInventory(String item) {
    PlayerValidator.validateIsInInventory(item);
    boolean isInInventory = inventory.contains(item.trim().toLowerCase());
    LOGGER.info("Checked for item in inventory: " + item.trim().toLowerCase() + ". Result: "
        + isInInventory);
    return isInInventory;
  }

  /**
   * Resets the player's state. Useful for starting a new game or round. This will reset player's
   * health, score, gold to default values and clear the inventory.
   */
  public void resetPlayer() {
    this.health = PlayerStatEnum.DEFAULT_HEALTH.getValue();
    this.score = PlayerStatEnum.DEFAULT_SCORE.getValue();
    this.gold = PlayerStatEnum.DEFAULT_GOLD.getValue();
    this.inventory.clear();
  }


  /**
   * Overrides the default toString method. Provides a string representation of the Player object,
   * displaying the name, health, score, gold, and inventory.
   *
   * @return String - string representation of the player
   */
  @Override
  public String toString() {
    return "Player: " + name + ", Health: " + health + ", Score: " + score +
        ", Gold: " + gold + ", Inventory: " + inventory.toString();
  }
}
