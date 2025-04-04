package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.exceptions.PlayerValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The type Player validator.
 */
public class PlayerValidator {

  // These could be placed in a configuration file or a constants class.
  private static final String BAD_ENGLISH_USER_NAMES_PATH = "src/main/resources/badUserNames/badEnglishUserNames";
  private static final String BAD_NORWEGIAN_USER_NAMES_PATH = "src/main/resources/badUserNames/badNorwegianUserNames";
  private static final int MAX_INVENTORY_SIZE = 100;

  private static final List<String> badEnglishUserNames;
  private static final List<String> badNorwegianUserNames;

  static {
    try {
      badEnglishUserNames = Files.readAllLines(Paths.get(BAD_ENGLISH_USER_NAMES_PATH));
      badNorwegianUserNames = Files.readAllLines(Paths.get(BAD_NORWEGIAN_USER_NAMES_PATH));
    } catch (IOException e) {
      throw new ExceptionInInitializerError("Error reading username files");
    }
  }

  /**
   * Validate player.
   *
   * @param name      the name
   * @param health    the health
   * @param score     the score
   * @param gold      the gold
   * @param inventory the inventory
   * @throws PlayerValidationException the player validation exception
   */
  public static void validatePlayer(String name, int health, int score, int gold,
      List<String> inventory)
      throws PlayerValidationException {

    validateName(name);
    validateHealth(health);
    validateScore(score);
    validateGold(gold);
    validateInventory(inventory);
  }

  private static void validateName(String name) throws PlayerValidationException {
    if (name == null || name.trim().isEmpty()) {
      throw new PlayerValidationException("Name cannot be null or blank");
    }

    if (name.length() < 3 || name.length() > 20) {
      throw new PlayerValidationException("Name must be between 3 and 20 characters");
    }

    if (!name.matches("[a-zA-Z0-9_]+")) {
      throw new PlayerValidationException(
          "Name can only contain alphanumeric characters and underscores");
    }

    if (badEnglishUserNames.stream().anyMatch(name::equalsIgnoreCase) ||
        badNorwegianUserNames.stream().anyMatch(name::equalsIgnoreCase)) {
      throw new PlayerValidationException(name + " is on the list of banned usernames");
    }
  }

  private static void validateHealth(int health) throws PlayerValidationException {
    if (health <= 0) {
      throw new PlayerValidationException("Starting health can not be zero or lower");
    }
  }

  private static void validateScore(int score) throws PlayerValidationException {
    if (score < 0) {
      throw new PlayerValidationException("Score cannot be negative");
    }
  }

  private static void validateGold(int gold) throws PlayerValidationException {
    if (gold < 0) {
      throw new PlayerValidationException("Gold cannot be negative");
    }
  }

  private static void validateInventory(List<String> inventory) throws PlayerValidationException {
    if (inventory == null) {
      throw new PlayerValidationException("Inventory cannot be null");
    }

    if (inventory.size() > MAX_INVENTORY_SIZE) {
      throw new PlayerValidationException(
          "Inventory cannot contain more than " + MAX_INVENTORY_SIZE + " items");
    }

    for (String item : inventory) {
      if (item == null || item.trim().isEmpty()) {
        throw new PlayerValidationException("Inventory item cannot be null or blank");
      }
    }
  }

  public static void validateAddToInventory(String item) throws PlayerValidationException {
    if (item == null) {
      throw new PlayerValidationException("added item can not be null");
    }
    if (item.isEmpty() || item.isBlank()) {
      throw new PlayerValidationException("added item can not be empty");
    }
  }

  public static void validateIsInInventory(String item) {
    if (item == null) {
      throw new PlayerValidationException("Item in inventory can not be null");
    }
    if (item.isEmpty() || item.isBlank()) {
      throw new PlayerValidationException("Item in inventory can not be empty");
    }
  }
}