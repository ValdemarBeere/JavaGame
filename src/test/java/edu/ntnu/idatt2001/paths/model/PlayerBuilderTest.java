package edu.ntnu.idatt2001.paths.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.game.GameDifficultyEnum;
import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.player.PlayerBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PlayerBuilderTest {

  private PlayerBuilder playerBuilder;
  private String name;
  private int health;
  private int score;
  private int gold;
  private List<String> inventory;
  private Image image;

  @BeforeEach
  public void setUp() {
    name = "TestPlayer";
    health = 100;
    score = 200;
    gold = 300;
    inventory = Arrays.asList("item1", "item2", "item3");
    image = null;  // Set your image here

    playerBuilder = new PlayerBuilder(GameDifficultyEnum.DEFAULT)
        .setName(name)
        .setHealth(health)
        .setScore(score)
        .setGold(gold)
        .setInventory(inventory)
        .setImage(image);
  }

  @Test
  public void builder_returns_correct_player() {
    Player player = playerBuilder.build();

    assertEquals(name, player.getName());
    assertEquals(health, player.getHealth());
    assertEquals(score, player.getScore());
    assertEquals(gold, player.getGold());
    assertEquals(inventory, player.getInventory());
    assertEquals(image, player.getImage());
  }

  @Test
  public void builder_returns_correct_player_with_default_values() {
    playerBuilder = new PlayerBuilder();
    Player player = playerBuilder.build();

    assertEquals("Player", player.getName());
    assertEquals(GameDifficultyEnum.DEFAULT.getDefaultHealth(), player.getHealth());
    assertEquals(GameDifficultyEnum.DEFAULT.getDefaultScore(), player.getScore());
    assertEquals(GameDifficultyEnum.DEFAULT.getDefaultGold(), player.getGold());
    assertTrue(player.getInventory().isEmpty());
    assertNull(player.getImage());
  }

  @Test
  public void builder_accepts_empty_inventory() {
    playerBuilder.setInventory(Collections.emptyList());
    Player player = playerBuilder.build();
    assertTrue(player.getInventory().isEmpty());
  }

  // Add more tests as needed
}
